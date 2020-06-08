package com.mairwunnx.projectessentials.home.commands

import com.mairwunnx.projectessentials.core.api.v1.MESSAGE_CORE_PREFIX
import com.mairwunnx.projectessentials.core.api.v1.commands.CommandAPI
import com.mairwunnx.projectessentials.core.api.v1.commands.CommandBase
import com.mairwunnx.projectessentials.core.api.v1.extensions.getPlayer
import com.mairwunnx.projectessentials.core.api.v1.extensions.isPlayerSender
import com.mairwunnx.projectessentials.core.api.v1.extensions.playerName
import com.mairwunnx.projectessentials.core.api.v1.messaging.MessagingAPI
import com.mairwunnx.projectessentials.core.api.v1.messaging.ServerMessagingAPI
import com.mairwunnx.projectessentials.core.api.v1.permissions.hasPermission
import com.mairwunnx.projectessentials.home.enums.HomeSelectStrategy
import com.mairwunnx.projectessentials.home.homeSettingsConfiguration
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import org.apache.logging.log4j.LogManager

object ConfigureHomeCommand : CommandBase(configureHomeLiteral, false) {
    override val name = "configure-home"

    fun respawnAtHomeAfterDeath(context: CommandContext<CommandSource>): Int {
        validate(
            context,
            "ess.configure.home.respawn-at-home-after-death",
            "respawn-at-home-after-death"
        ) {
            val value = CommandAPI.getBool(context, "value")
            val oldValue = homeSettingsConfiguration.respawnAtHomeAfterDeath
            homeSettingsConfiguration.respawnAtHomeAfterDeath = value
            changed(
                context, "respawn-at-home-after-death", oldValue.toString(), value.toString()
            ).also { super.process(context) }
        }
        return 0
    }

    fun playSoundOnTeleport(context: CommandContext<CommandSource>): Int {
        validate(
            context,
            "ess.configure.home.play-sound-on-teleport",
            "play-sound-on-teleport"
        ) {
            val value = CommandAPI.getBool(context, "value")
            val oldValue = homeSettingsConfiguration.playSoundOnTeleport
            homeSettingsConfiguration.playSoundOnTeleport = value
            changed(
                context, "play-sound-on-teleport", oldValue.toString(), value.toString()
            ).also { super.process(context) }
        }
        return 0
    }

    fun showEffectOnTeleport(context: CommandContext<CommandSource>): Int {
        validate(
            context,
            "ess.configure.home.show-effect-on-teleport",
            "show-effect-on-teleport"
        ) {
            val value = CommandAPI.getBool(context, "value")
            val oldValue = homeSettingsConfiguration.showEffectOnTeleport
            homeSettingsConfiguration.showEffectOnTeleport = value
            changed(
                context, "show-effect-on-teleport", oldValue.toString(), value.toString()
            ).also { super.process(context) }
        }
        return 0
    }

    fun respawnHomeSelectStrategy(context: CommandContext<CommandSource>): Int {
        validate(
            context,
            "ess.configure.home.respawn-home-select-strategy",
            "respawn-home-select-strategy"
        ) {
            val value = context.getArgument("value", HomeSelectStrategy::class.java)
            val oldValue = homeSettingsConfiguration.respawnHomeSelectStrategy
            homeSettingsConfiguration.respawnHomeSelectStrategy = value
            changed(
                context, "respawn-home-select-strategy", oldValue.toString(), value.toString()
            ).also { super.process(context) }
        }
        return 0
    }

    private fun validate(
        context: CommandContext<CommandSource>,
        node: String,
        setting: String,
        action: (isServer: Boolean) -> Unit
    ) = context.getPlayer()?.let {
        if (hasPermission(it, node, 4)) {
            action(false)
        } else {
            MessagingAPI.sendMessage(
                context.getPlayer()!!,
                "$MESSAGE_CORE_PREFIX.configure.restricted",
                args = *arrayOf(setting)
            )
        }
    } ?: run { action(true) }

    private fun changed(
        context: CommandContext<CommandSource>,
        setting: String,
        oldValue: String,
        value: String
    ) = if (context.isPlayerSender()) {
        LogManager.getLogger().info(
            "Setting name `$setting` value changed by ${context.playerName()} from `$oldValue` to $value"
        )
        MessagingAPI.sendMessage(
            context.getPlayer()!!,
            "$MESSAGE_CORE_PREFIX.configure.successfully",
            args = *arrayOf(setting, oldValue, value)
        )
    } else {
        ServerMessagingAPI.response {
            "Setting name `$setting` value changed from `$oldValue` to $value"
        }
    }
}
