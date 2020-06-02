package com.mairwunnx.projectessentials.home.commands

import com.mairwunnx.projectessentials.core.api.v1.configuration.ConfigurationAPI.getConfigurationByName
import com.mairwunnx.projectessentials.core.api.v1.extensions.getPlayer
import com.mairwunnx.projectessentials.core.api.v1.extensions.isPlayerSender
import com.mairwunnx.projectessentials.home.configurations.HomeConfiguration
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.command.ISuggestionProvider

val homeLiteral: LiteralArgumentBuilder<CommandSource> =
    literal<CommandSource>("home").then(
        Commands.argument(
            "home", StringArgumentType.string()
        ).suggests { ctx, builder ->
            ISuggestionProvider.suggest(
                getConfigurationByName<HomeConfiguration>(
                    "home"
                ).take().let { model ->
                    if (ctx.isPlayerSender()) {
                        model.users.asSequence().find {
                            it.name == ctx.getPlayer()!!.name.string || it.uuid == ctx.getPlayer()!!.uniqueID.toString()
                        }?.homes?.asSequence()?.map { it.home }?.toList() ?: emptyList<String>()
                    } else emptyList<String>()
                }, builder
            )
        }
    )
