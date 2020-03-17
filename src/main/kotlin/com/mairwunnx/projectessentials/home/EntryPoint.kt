package com.mairwunnx.projectessentials.home

import com.mairwunnx.projectessentials.core.EssBase
import com.mairwunnx.projectessentials.core.configuration.localization.LocalizationConfigurationUtils
import com.mairwunnx.projectessentials.core.localization.processLocalizations
import com.mairwunnx.projectessentials.home.api.HomeAPI
import com.mairwunnx.projectessentials.home.commands.DelHomeCommand
import com.mairwunnx.projectessentials.home.commands.HomeCommand
import com.mairwunnx.projectessentials.home.commands.SetHomeCommand
import com.mairwunnx.projectessentials.home.storage.StorageBase
import com.mairwunnx.projectessentials.permissions.permissions.PermissionsAPI
import com.mojang.brigadier.CommandDispatcher
import net.minecraft.command.CommandSource
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent
import org.apache.logging.log4j.LogManager

@Suppress("unused")
@Mod("project_essentials_home")
internal class EntryPoint : EssBase() {
    private val logger = LogManager.getLogger()

    init {
        modInstance = this
        modVersion = "1.14.4-1.2.0"
        logBaseInfo()
        validateForgeVersion()
        MinecraftForge.EVENT_BUS.register(this)
        StorageBase.loadUserData()
        loadLocalization()
    }

    private fun loadLocalization() {
        if (LocalizationConfigurationUtils.getConfig().enabled) {
            processLocalizations(
                EntryPoint::class.java, listOf(
                    "/assets/projectessentialshome/lang/en_us.json",
                    "/assets/projectessentialshome/lang/ru_ru.json",
                    "/assets/projectessentialshome/lang/de_de.json"
                )
            )
        }
    }

    @SubscribeEvent
    fun onServerStarting(event: FMLServerStartingEvent) {
        registerCommands(event.server.commandManager.dispatcher)
    }

    private fun registerCommands(
        cmdDispatcher: CommandDispatcher<CommandSource>
    ) {
        loadAdditionalModules()
        HomeCommand.register(cmdDispatcher)
        SetHomeCommand.register(cmdDispatcher)
        DelHomeCommand.register(cmdDispatcher)
    }

    @Suppress("UNUSED_PARAMETER")
    @SubscribeEvent
    fun onServerStopping(it: FMLServerStoppingEvent) {
        HomeAPI.save()
    }

    private fun loadAdditionalModules() {
        try {
            Class.forName(cooldownAPIClassPath)
            cooldownsInstalled = true
        } catch (_: ClassNotFoundException) {
            // ignored
        }

        try {
            Class.forName(permissionAPIClassPath)
            permissionsInstalled = true
        } catch (_: ClassNotFoundException) {
            // ignored
        }
    }

    companion object {
        lateinit var modInstance: EntryPoint
        var cooldownsInstalled: Boolean = false
        var permissionsInstalled: Boolean = false

        fun hasPermission(player: ServerPlayerEntity, node: String, opLevel: Int = 0): Boolean =
            if (permissionsInstalled) {
                PermissionsAPI.hasPermission(player.name.string, node)
            } else {
                player.hasPermissionLevel(opLevel)
            }
    }
}
