package com.mairwunnx.projectessentials.projectessentialshome.commands

import com.mairwunnx.projectessentials.cooldown.essentials.CommandsAliases
import com.mairwunnx.projectessentials.core.extensions.isPlayerSender
import com.mairwunnx.projectessentials.core.extensions.sendMsg
import com.mairwunnx.projectessentials.core.helpers.ONLY_PLAYER_CAN
import com.mairwunnx.projectessentials.core.helpers.PERMISSION_LEVEL
import com.mairwunnx.projectessentials.projectessentialshome.EntryPoint
import com.mairwunnx.projectessentials.projectessentialshome.EntryPoint.Companion.hasPermission
import com.mairwunnx.projectessentials.projectessentialshome.models.HomeModel
import com.mairwunnx.projectessentials.projectessentialshome.storage.StorageBase
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import org.apache.logging.log4j.LogManager

object SetHomeCommand {
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

                if (!override) {
                    homeModel.forEach {
                        if (it.home == homeName) {
                            sendMsg("home", c.source, "home.set.already_exist", homeName)
                            return 0
                        }
                    }
                }

                homeModel.add(
                    HomeModel.Home(
                        homeName, clientWorld, worldId, xPos, yPos, zPos, yaw, pitch
                    )
                )
                StorageBase.setData(playerUUID, HomeModel(homeModel))
                sendMsg("home", c.source, "home.set.success", homeName)
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
