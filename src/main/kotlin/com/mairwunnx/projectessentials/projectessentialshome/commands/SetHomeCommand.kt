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

object SetHomeCommand {
    private val aliases = arrayOf(
        "sethome", "esethome"
    )
    private val logger = LogManager.getLogger()

    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        logger.info("    - register \"/sethome\" command ...")
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
            CommandsAliases.aliases["sethome"] = aliases.toMutableList()
            logger.info("        - applying aliases: $aliases")
        } catch (_: ClassNotFoundException) {
            // ignored
        }
    }

    private fun execute(c: CommandContext<CommandSource>): Int {
        if (c.isPlayerSender()) {
            val player = c.source.asPlayer()
            if (PermissionsAPI.hasPermission(player.name.string, "ess.home.set")) {
                val playerUUID = player.uniqueID.toString()
                val homeName: String = try {
                    StringArgumentType.getString(c, "home name")
                } catch (_: IllegalArgumentException) {
                    "home"
                }
                val clientWorld = c.source.world.worldInfo.worldName
                val worldId = c.source.world.worldType.id
                val xPos = player.posX.toInt()
                val yPos = player.posY.toInt()
                val zPos = player.posZ.toInt()
                val yaw = player.rotationYaw
                val pitch = player.rotationPitch
                val homeModel = StorageBase.getData(playerUUID).homes
                homeModel.add(
                    HomeModel.Home(
                        homeName, clientWorld, worldId, xPos, yPos, zPos, yaw, pitch
                    )
                )
                StorageBase.setData(playerUUID, HomeModel(homeModel))
                sendMsg("home", c.source, "home.set.success")
                logger.info("New home point for ${player.name.string} installed with data: ")
                logger.info("    - name: $homeName")
                logger.info("    - world / world id: $clientWorld / $worldId")
                logger.info("    - xpos: $xPos")
                logger.info("    - ypos: $yPos")
                logger.info("    - zpos: $zPos")
                logger.info("    - yaw: $yaw")
                logger.info("    - pitch: $pitch")
                logger.info("Executed command \"/sethome\" from ${player.name.string}")
            } else {
                sendMsg("home", c.source, "home.set.restricted")
                logger.info(
                    PERMISSION_LEVEL
                        .replace("%0", player.name.string)
                        .replace("%1", "sethome")
                )
            }
        } else {
            logger.info(ONLY_PLAYER_CAN.replace("%0", "sethome"))
        }
        return 0
    }
}
