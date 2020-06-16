package com.mairwunnx.projectessentials.home.commands

import com.mairwunnx.projectessentials.core.api.v1.MESSAGE_MODULE_PREFIX
import com.mairwunnx.projectessentials.core.api.v1.commands.CommandAPI
import com.mairwunnx.projectessentials.core.api.v1.commands.CommandBase
import com.mairwunnx.projectessentials.core.api.v1.extensions.getPlayer
import com.mairwunnx.projectessentials.core.api.v1.messaging.MessagingAPI
import com.mairwunnx.projectessentials.core.api.v1.messaging.ServerMessagingAPI
import com.mairwunnx.projectessentials.home.helpers.validateAndExecute
import com.mairwunnx.projectessentials.home.homeConfiguration
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource

object DelHomeCommand : CommandBase(delHomeLiteral, false) {
    override val name = "del-home"

    override fun process(context: CommandContext<CommandSource>) = 0.also {
        fun out(status: String, vararg args: String) = MessagingAPI.sendMessage(
            context.getPlayer()!!, "${MESSAGE_MODULE_PREFIX}home.delhome.$status", args = *args
        )

        validateAndExecute(context, "ess.home.remove", 0) { isServer ->
            if (isServer) {
                ServerMessagingAPI.throwOnlyPlayerCan()
            } else {
                val player = context.getPlayer()!!
                val name = if (CommandAPI.getStringExisting(context, "home")) {
                    CommandAPI.getString(context, "home")
                } else "home"

                homeConfiguration.users.asSequence().find {
                    it.name == player.name.string || it.uuid == player.uniqueID.toString()
                }?.let { user ->
                    user.homes.asSequence().find {
                        it.home == name
                    }?.let { home ->
                        user.homes.removeIf { it.home == home.home }.also {
                            out("success", name).also { super.process(context) }
                        }
                    } ?: run { out("not_found", name) }
                } ?: run { out("not_found", name) }
            }
        }
    }
}
