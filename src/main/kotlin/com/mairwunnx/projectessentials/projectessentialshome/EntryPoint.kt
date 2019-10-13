package com.mairwunnx.projectessentials.projectessentialshome

import com.mairwunnx.projectessentials.projectessentialshome.storage.StorageBase
import com.mairwunnx.projectessentialscore.EssBase
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent
import org.apache.logging.log4j.LogManager

@Suppress("unused")
@Mod("project_essentials_home")
class EntryPoint : EssBase() {
    private val logger = LogManager.getLogger()

    init {
        modInstance = this
        modVersion = "1.14.4-1.0.0.0"
        logBaseInfo()
        validateForgeVersion()
        logger.debug("Register event bus for $modName mod ...")
        MinecraftForge.EVENT_BUS.register(this)
        logger.info("Loading modification user home data ...")
        StorageBase.loadUserData()
    }

    @Suppress("UNUSED_PARAMETER")
    @SubscribeEvent
    fun onServerStopping(it: FMLServerStoppingEvent) {
        logger.info("Shutting down $modName mod ...")
        logger.info("    - Saving modification user home data ...")
        StorageBase.saveUserData()
    }

    companion object {
        lateinit var modInstance: EntryPoint
    }
}
