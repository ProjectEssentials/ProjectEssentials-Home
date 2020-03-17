package com.mairwunnx.projectessentials.home.commands

import com.mairwunnx.projectessentials.cooldown.essentials.CommandsAliases
import com.mairwunnx.projectessentials.core.extensions.isPlayerSender
import com.mairwunnx.projectessentials.core.helpers.throwOnlyPlayerCan
import com.mairwunnx.projectessentials.core.helpers.throwPermissionLevel
import com.mairwunnx.projectessentials.home.EntryPoint
import com.mairwunnx.projectessentials.home.EntryPoint.Companion.hasPermission
import com.mairwunnx.projectessentials.home.api.HomeAPI
import com.mairwunnx.projectessentials.home.sendMessage
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import org.apache.logging.log4j.LogManager

internal object DelHomeCommand {
    private val aliases = arrayOf("delhome", "edelhome", "removehome", "remhome")
    private val logger = LogManager.getLogger()

    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        logger.info("Register \"/delhome\" command")
        aliases.forEach { command ->
            dispatcher.register(
                literal<CommandSource>(command).executes {
                    return@executes execute(it)
                }.then(
                    Commands.argument(
                        "home name", StringArgumentType.string()
                    ).executes {
                        return@executes execute(it)
                    }
                )
            )
        }
        applyCommandAliases()
    }

    private fun applyCommandAliases() {
        if (!EntryPoint.cooldownsInstalled) return
        CommandsAliases.aliases["delhome"] = aliases.toMutableList()
    }

    private fun execute(c: CommandContext<CommandSource>): Int {
        if (c.isPlayerSender()) {
            val player = c.source.asPlayer()
            if (hasPermission(player, "ess.home.remove")) {
                val homeName: String = try {
                    StringArgumentType.getString(c, "home name")
                } catch (_: IllegalArgumentException) {
                    "home"
                }

                val result = HomeAPI.remove(player, homeName)
                if (result) {
                    sendMessage(c.source, "remove.success", homeName)
                    logger.info("Executed command \"/delhome\" from ${player.name.string}")
                    return 0
                }

                sendMessage(c.source, "not_found", homeName)
            } else {
                sendMessage(c.source, "remove.restricted")
                throwPermissionLevel(player.name.string, "delhome")
            }
        } else {
            throwOnlyPlayerCan("delhome")
        }
        return 0
    }
}
