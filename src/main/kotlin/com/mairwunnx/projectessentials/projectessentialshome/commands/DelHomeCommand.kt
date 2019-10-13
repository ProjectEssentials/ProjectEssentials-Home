package com.mairwunnx.projectessentials.projectessentialshome.commands

import com.mairwunnx.projectessentials.projectessentialshome.models.HomeModel
import com.mairwunnx.projectessentials.projectessentialshome.storage.StorageBase
import com.mairwunnx.projectessentialscooldown.essentials.CommandsAliases
import com.mairwunnx.projectessentialscore.extensions.isPlayerSender
import com.mairwunnx.projectessentialscore.extensions.sendMsg
import com.mairwunnx.projectessentialscore.helpers.ONLY_PLAYER_CAN
import com.mairwunnx.projectessentialscore.helpers.PERMISSION_LEVEL
import com.mairwunnx.projectessentialspermissions.permissions.PermissionsAPI
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import org.apache.logging.log4j.LogManager

@Suppress("DuplicatedCode")
object DelHomeCommand {
    private val aliases = arrayOf("delhome", "edelhome", "removehome", "remhome")
    private val logger = LogManager.getLogger()

    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        logger.info("    - register \"/delhome\" command ...")
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
        try {
            Class.forName(
                "com.mairwunnx.projectessentialscooldown.essentials.CommandsAliases"
            )
            CommandsAliases.aliases["delhome"] = aliases.toMutableList()
            logger.info("        - applying aliases: $aliases")
        } catch (_: ClassNotFoundException) {
            // ignored
        }
    }

    private fun execute(c: CommandContext<CommandSource>): Int {
        if (c.isPlayerSender()) {
            val player = c.source.asPlayer()
            if (PermissionsAPI.hasPermission(player.name.string, "ess.home.remove")) {
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
