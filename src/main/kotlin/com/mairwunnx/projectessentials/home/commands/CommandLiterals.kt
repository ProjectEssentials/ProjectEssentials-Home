@file:Suppress("DuplicatedCode")

package com.mairwunnx.projectessentials.home.commands

import com.mairwunnx.projectessentials.core.api.v1.extensions.getPlayer
import com.mairwunnx.projectessentials.core.api.v1.extensions.isPlayerSender
import com.mairwunnx.projectessentials.home.homeConfiguration
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.command.ISuggestionProvider

inline val homeLiteral: LiteralArgumentBuilder<CommandSource>
    get() = literal<CommandSource>("home").then(
        Commands.argument(
            "home", StringArgumentType.string()
        ).suggests { ctx, builder ->
            ISuggestionProvider.suggest(
                homeConfiguration.let { model ->
                    if (ctx.isPlayerSender()) {
                        model.users.asSequence().find {
                            it.name == ctx.getPlayer()!!.name.string || it.uuid == ctx.getPlayer()!!.uniqueID.toString()
                        }?.homes?.asSequence()?.map { it.home }?.toList() ?: emptyList<String>()
                    } else emptyList<String>()
                }, builder
            )
        }.executes { HomeCommand.process(it) }
    ).executes { HomeCommand.process(it) }

inline val setHomeLiteral: LiteralArgumentBuilder<CommandSource>
    get() = literal<CommandSource>("set-home").then(
        Commands.argument(
            "home", StringArgumentType.string()
        ).executes { SetHomeCommand.process(it) }
    ).executes { SetHomeCommand.process(it) }

inline val delHomeLiteral: LiteralArgumentBuilder<CommandSource>
    get() = literal<CommandSource>("del-home").then(
        Commands.argument(
            "home", StringArgumentType.string()
        ).suggests { ctx, builder ->
            ISuggestionProvider.suggest(
                homeConfiguration.let { model ->
                    if (ctx.isPlayerSender()) {
                        model.users.asSequence().find {
                            it.name == ctx.getPlayer()!!.name.string || it.uuid == ctx.getPlayer()!!.uniqueID.toString()
                        }?.homes?.asSequence()?.map { it.home }?.toList() ?: emptyList<String>()
                    } else emptyList<String>()
                }, builder
            )
        }.executes { DelHomeCommand.process(it) }
    ).executes { DelHomeCommand.process(it) }
