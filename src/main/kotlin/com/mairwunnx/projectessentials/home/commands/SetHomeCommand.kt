package com.mairwunnx.projectessentials.home.commands

import com.mairwunnx.projectessentials.core.api.v1.MESSAGE_MODULE_PREFIX
import com.mairwunnx.projectessentials.core.api.v1.commands.CommandAPI
import com.mairwunnx.projectessentials.core.api.v1.commands.CommandBase
import com.mairwunnx.projectessentials.core.api.v1.extensions.currentDimensionId
import com.mairwunnx.projectessentials.core.api.v1.extensions.getPlayer
import com.mairwunnx.projectessentials.core.api.v1.messaging.MessagingAPI
import com.mairwunnx.projectessentials.core.api.v1.messaging.ServerMessagingAPI
import com.mairwunnx.projectessentials.core.api.v1.permissions.hasPermission
import com.mairwunnx.projectessentials.home.configurations.HomeConfigurationModel
import com.mairwunnx.projectessentials.home.helpers.validateAndExecute
import com.mairwunnx.projectessentials.home.homeConfiguration
import com.mairwunnx.projectessentials.home.homeSettingsConfiguration
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.entity.player.ServerPlayerEntity

object SetHomeCommand : CommandBase(setHomeLiteral, false) {
    override val name = "set-home"
    override fun process(context: CommandContext<CommandSource>) = 0.also {
        fun out(status: String, vararg args: String) = MessagingAPI.sendMessage(
            context.getPlayer()!!, "${MESSAGE_MODULE_PREFIX}home.sethome.$status", args = *args
        )

        validateAndExecute(context, "ess.home.set", 0) { isServer ->
            if (isServer) {
                ServerMessagingAPI.throwOnlyPlayerCan()
            } else {
                val player = context.getPlayer()!!
                val name = if (CommandAPI.getStringExisting(context, "home")) {
                    CommandAPI.getString(context, "home")
                } else "home"

                fun commit(user: HomeConfigurationModel.User?) {
                    fun fromUser() = HomeConfigurationModel.User.Home(
                        name, player.currentDimensionId,
                        player.position.x, player.position.y, player.position.z,
                        player.rotationYaw, player.rotationPitch
                    ).also { out("success", name).also { super.process(context) } }

                    if (!hasLimitations(player, user)) {
                        user?.homes?.add(fromUser()) ?: run {
                            homeConfiguration.users.add(
                                HomeConfigurationModel.User(
                                    player.name.string,
                                    player.uniqueID.toString(),
                                    mutableListOf(fromUser())
                                )
                            )
                        }
                    } else out("limit")
                }

                homeConfiguration.users.asSequence().find {
                    it.name == player.name.string || it.uuid == player.uniqueID.toString()
                }?.let { user ->
                    user.homes.asSequence().find {
                        it.home == name
                    }?.let { out("exist", name) } ?: run { commit(user) }
                } ?: run { commit(null) }
            }
        }
    }

    private fun hasLimitations(
        player: ServerPlayerEntity,
        user: HomeConfigurationModel.User?
    ): Boolean {
        if (hasPermission(player, "ess.home.limit.except", 4)) return false
        var allowed = 1
        homeSettingsConfiguration.homeLimitations.also { map ->
            homeSettingsConfiguration.homeLimitations.keys.asSequence().forEach {
                if (hasPermission(player, "ess.home.limit.$it", 0)) {
                    if (allowed < map.getValue(it)) allowed = map.getValue(it)
                }
            }
        }
        return if (user == null) false else user.homes.count() >= allowed
    }
}
