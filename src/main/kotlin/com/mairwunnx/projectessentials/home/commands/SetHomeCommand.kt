package com.mairwunnx.projectessentials.home.commands

import com.mairwunnx.projectessentials.cooldown.essentials.CommandsAliases
import com.mairwunnx.projectessentials.core.extensions.isPlayerSender
import com.mairwunnx.projectessentials.core.extensions.sendMsg
import com.mairwunnx.projectessentials.core.helpers.throwOnlyPlayerCan
import com.mairwunnx.projectessentials.core.helpers.throwPermissionLevel
import com.mairwunnx.projectessentials.home.EntryPoint
import com.mairwunnx.projectessentials.home.EntryPoint.Companion.hasPermission
import com.mairwunnx.projectessentials.home.api.HomeAPI
import com.mairwunnx.projectessentials.home.sendMessage
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import org.apache.logging.log4j.LogManager

internal object SetHomeCommand {
    private val aliases = arrayOf("sethome", "esethome")
    private val logger = LogManager.getLogger()

    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        logger.info("Register \"/sethome\" command")
        applyCommandAliases()

        aliases.forEach { command ->
            dispatcher.register(
                literal<CommandSource>(command).executes {
                    return@executes execute(it)
                }.then(
                    Commands.argument(
                        "home name", StringArgumentType.string()
                    ).executes {
                        return@executes execute(it)
                    }.then(
                        Commands.argument(
                            "override", BoolArgumentType.bool()
                        ).executes {
                            return@executes execute(
                                it, BoolArgumentType.getBool(it, "override")
                            )
                        }
                    )
                )
            )
        }
    }

    private fun applyCommandAliases() {
        if (!EntryPoint.cooldownsInstalled) return
        CommandsAliases.aliases["sethome"] = aliases.toMutableList()
    }

    private fun execute(
        c: CommandContext<CommandSource>,
        override: Boolean = false
    ): Int {
        if (c.isPlayerSender()) {
            val player = c.source.asPlayer()
            if (hasPermission(player, "ess.home.set")) {
                val homeName: String = try {
                    StringArgumentType.getString(c, "home name")
                } catch (_: IllegalArgumentException) {
                    "home"
                }

                val result = HomeAPI.create(player, homeName, override)
                if (!result) {
                    sendMessage(c.source, "set.already_exist", homeName)
                    return 0
                }

                sendMessage(c.source, "set.success", homeName)
                logger.info("Executed command \"/sethome\" from ${player.name.string}")
            } else {
                sendMsg("home", c.source, "home.set.restricted")
                throwPermissionLevel(player.name.string, "sethome")
            }
        } else {
            throwOnlyPlayerCan("sethome")
        }
        return 0
    }
}
