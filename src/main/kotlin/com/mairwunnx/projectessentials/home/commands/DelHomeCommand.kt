package com.mairwunnx.projectessentials.home.commands

import com.mairwunnx.projectessentials.cooldown.essentials.CommandsAliases
import com.mairwunnx.projectessentials.core.extensions.isPlayerSender
import com.mairwunnx.projectessentials.core.extensions.sendMsg
import com.mairwunnx.projectessentials.core.helpers.ONLY_PLAYER_CAN
import com.mairwunnx.projectessentials.core.helpers.PERMISSION_LEVEL
import com.mairwunnx.projectessentials.home.EntryPoint
import com.mairwunnx.projectessentials.home.EntryPoint.Companion.hasPermission
import com.mairwunnx.projectessentials.home.models.HomeModel
import com.mairwunnx.projectessentials.home.storage.StorageBase
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import org.apache.logging.log4j.LogManager

object DelHomeCommand {
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
                val playerUUID = player.uniqueID.toString()
                val homeName: String = try {
                    StringArgumentType.getString(c, "home name")
                } catch (_: IllegalArgumentException) {
                    "home"
                }
                val homeModel = StorageBase.getData(playerUUID).homes
                StorageBase.getData(playerUUID).homes.forEach {
                    if (it.home == homeName) {
                        homeModel.remove(it)
                        StorageBase.setData(playerUUID, HomeModel(homeModel))
                        sendMsg("home", c.source, "home.remove.success", homeName)
                        logger.info("Executed command \"/delhome\" from ${player.name.string}")
                        return 0
                    }
                }
                sendMsg("home", c.source, "home.not_found", homeName)
            } else {
                sendMsg("home", c.source, "home.remove.restricted")
                logger.info(
                    PERMISSION_LEVEL
                        .replace("%0", player.name.string)
                        .replace("%1", "delhome")
                )
            }
        } else {
            logger.info(ONLY_PLAYER_CAN.replace("%0", "delhome"))
        }
        return 0
    }
}
