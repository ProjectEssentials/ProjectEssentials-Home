package com.mairwunnx.projectessentials.projectessentialshome

import com.mairwunnx.projectessentials.core.EssBase
import com.mairwunnx.projectessentials.permissions.permissions.PermissionsAPI
import com.mairwunnx.projectessentials.projectessentialshome.commands.DelHomeCommand
import com.mairwunnx.projectessentials.projectessentialshome.commands.HomeCommand
import com.mairwunnx.projectessentials.projectessentialshome.commands.SetHomeCommand
import com.mairwunnx.projectessentials.projectessentialshome.storage.StorageBase
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
class EntryPoint : EssBase() {
    private val logger = LogManager.getLogger()

    init {
        modInstance = this
        modVersion = "1.14.4-1.1.0"
        logBaseInfo()
        validateForgeVersion()
        MinecraftForge.EVENT_BUS.register(this)
        StorageBase.loadUserData()
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
        StorageBase.saveUserData()
    }

    private fun loadAdditionalModules() {
        try {
            Class.forName(
                "com.mairwunnx.projectessentials.cooldown.essentials.CommandsAliases"
            )
            cooldownsInstalled = true
        } catch (_: ClassNotFoundException) {
            // ignored
        }

        try {
            Class.forName(
                "com.mairwunnx.projectessentials.permissions.permissions.PermissionsAPI"
            )
            permissionsInstalled = true
        } catch (_: ClassNotFoundException) {
            // ignored
        }
    }

    companion object {
        lateinit var modInstance: EntryPoint
        var cooldownsInstalled: Boolean = false
        var permissionsInstalled: Boolean = false

        fun hasPermission(player: ServerPlayerEntity, node: String, opLevel: Int = 2): Boolean =
            if (permissionsInstalled) {
                PermissionsAPI.hasPermission(player.name.string, node)
            } else {
                player.server.opPermissionLevel >= opLevel
            }
    }
}
