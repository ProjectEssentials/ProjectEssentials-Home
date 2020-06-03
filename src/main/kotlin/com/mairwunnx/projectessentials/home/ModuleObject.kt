@file:Suppress("unused")

package com.mairwunnx.projectessentials.home

import com.mairwunnx.projectessentials.core.api.v1.IMCLocalizationMessage
import com.mairwunnx.projectessentials.core.api.v1.IMCProvidersMessage
import com.mairwunnx.projectessentials.core.api.v1.events.ModuleEventAPI.subscribeOn
import com.mairwunnx.projectessentials.core.api.v1.events.forge.ForgeEventType
import com.mairwunnx.projectessentials.core.api.v1.events.forge.InterModEnqueueEventData
import com.mairwunnx.projectessentials.core.api.v1.module.IModule
import com.mairwunnx.projectessentials.home.commands.DelHomeCommand
import com.mairwunnx.projectessentials.home.commands.HomeCommand
import com.mairwunnx.projectessentials.home.commands.SetHomeCommand
import com.mairwunnx.projectessentials.home.configurations.HomeConfiguration
import com.mairwunnx.projectessentials.home.configurations.HomeSettingsConfiguration
import com.mairwunnx.projectessentials.home.enums.HomeSelectStrategy.First
import com.mairwunnx.projectessentials.home.enums.HomeSelectStrategy.Last
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.InterModComms
import net.minecraftforge.fml.common.Mod

@Mod("project_essentials_home")
class ModuleObject : IModule {
    override val name = this::class.java.`package`.implementationTitle.split(" ").last()
    override val version = this::class.java.`package`.implementationVersion!!
    override val loadIndex = 4
    override fun init() = Unit

    init {
        EVENT_BUS.register(this)
        subscribeOn<InterModEnqueueEventData>(
            ForgeEventType.EnqueueIMCEvent
        ) {
            sendLocalizationRequest()
            sendProvidersRequest()
        }
    }

    @SubscribeEvent
    fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
        val player = event.player as ServerPlayerEntity
        homeConfiguration.users.asSequence().find {
            it.name == player.name.string || it.uuid == player.uniqueID.toString()
        }?.let {
            if (it.homes.isEmpty()) return
            when (homeSettingsConfiguration.respawnHomeSelectStrategy) {
                Last -> teleportToHome(player, it.homes.last())
                First -> teleportToHome(player, it.homes.first())
            }
        }
    }

    private fun sendLocalizationRequest() {
        InterModComms.sendTo(
            "project_essentials_core",
            IMCLocalizationMessage
        ) {
            fun() = mutableListOf(
                "/assets/projectessentialshome/lang/en_us.json",
                "/assets/projectessentialshome/lang/ru_ru.json",
                "/assets/projectessentialshome/lang/de_de.json",
                "/assets/projectessentialshome/lang/zh_cn.json"
            )
        }
    }

    private fun sendProvidersRequest() {
        InterModComms.sendTo(
            "project_essentials_core",
            IMCProvidersMessage
        ) {
            fun() = listOf(
                SetHomeCommand::class.java,
                HomeCommand::class.java,
                DelHomeCommand::class.java,
                HomeConfiguration::class.java,
                HomeSettingsConfiguration::class.java,
                ModuleObject::class.java
            )
        }
    }
}
