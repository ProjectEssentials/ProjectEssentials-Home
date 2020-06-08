package com.mairwunnx.projectessentials.home.configurations

import com.mairwunnx.projectessentials.core.api.v1.configuration.IConfiguration
import com.mairwunnx.projectessentials.core.api.v1.helpers.jsonInstance
import com.mairwunnx.projectessentials.core.api.v1.helpers.projectConfigDirectory
import net.minecraftforge.fml.server.ServerLifecycleHooks.getCurrentServer
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileNotFoundException

object HomeConfiguration : IConfiguration<HomeConfigurationModel> {
    private val logger = LogManager.getLogger()
    private var configurationData = HomeConfigurationModel()

    override val name = "home"
    override val version = 1
    override val configuration = take()
    override val path by lazy {
        projectConfigDirectory + File.separator + getCurrentServer().folderName + File.separator + "homes.json"
    }

    override fun load() {
        try {
            val configRaw = File(path).readText()
            configurationData = jsonInstance.parse(
                HomeConfigurationModel.serializer(), configRaw
            )
        } catch (ex: FileNotFoundException) {
            logger.error("Configuration file ($path) not found!")
            logger.warn("The default configuration will be used")
        }
    }

    override fun save() {
        File(path).parentFile.mkdirs()

        logger.info("Saving configuration `${name}`")
        val raw = jsonInstance.stringify(
            HomeConfigurationModel.serializer(), configurationData
        )
        try {
            File(path).writeText(raw)
        } catch (ex: SecurityException) {
            logger.error("An error occurred while saving $name configuration", ex)
        }
    }

    override fun take() = configurationData
}
