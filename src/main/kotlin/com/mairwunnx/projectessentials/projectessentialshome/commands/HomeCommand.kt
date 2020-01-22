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
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.world.dimension.DimensionType
import org.apache.logging.log4j.LogManager

@Suppress("DuplicatedCode")
object HomeCommand {
    private val aliases = arrayOf("home", "ehome")
    private val logger = LogManager.getLogger()

    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        logger.info("Register \"/home\" command ...")
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
                    }
                )
            )
        }
    }

    private fun applyCommandAliases() {
        if (!EntryPoint.cooldownsInstalled) return
        CommandsAliases.aliases["home"] = aliases.toMutableList()
    }

    private fun execute(c: CommandContext<CommandSource>): Int {
        if (c.isPlayerSender()) {
            val player = c.source.asPlayer()
            if (hasPermission(player, "ess.home")) {
                val playerUUID = player.uniqueID.toString()
                val homeName: String = try {
                    StringArgumentType.getString(c, "home name")
                } catch (_: IllegalArgumentException) {
                    "home"
                }
                val home = StorageBase.getData(playerUUID)
                home.homes.forEach {
                    if (it.home == homeName) {
                        moveToHome(player, it)
                        return 0
                    }
                }
                sendMsg("home", c.source, "home.not_found", homeName)
                logger.info("Player ${player.name.string} try teleport to not exist home $homeName")
            } else {
                sendMsg("home", c.source, "home.restricted")
                logger.info(
                    PERMISSION_LEVEL
                        .replace("%0", player.name.string)
                        .replace("%1", "home")
                )
            }
        } else {
            logger.info(ONLY_PLAYER_CAN.replace("%0", "home"))
        }
        return 0
    }

    private fun moveToHome(player: ServerPlayerEntity, home: HomeModel.Home) {
        val xPos = home.xPos.toDouble()
        val yPos = home.yPos.toDouble()
        val zPos = home.zPos.toDouble()
        val yaw = home.yaw
        val pitch = home.pitch
        val dimId = home.worldId
        val clientWorld = home.clientWorld
        val targetWorld = player.server.getWorld(
            DimensionType.getById(dimId) ?: DimensionType.OVERWORLD
        )
        if (player.world.worldInfo.worldName == clientWorld) {
            player.teleport(targetWorld, xPos, yPos, zPos, yaw, pitch)
            sendMsg("home", player.commandSource, "home.success", home.home)
        } else {
            sendMsg("home", player.commandSource, "home.not_found", home.home)
            logger.info("Player ${player.name.string} try teleport to not exist home ${home.home}")
        }
    }
}
