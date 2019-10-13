package com.mairwunnx.projectessentials.projectessentialshome.storage

import com.mairwunnx.projectessentials.projectessentialshome.models.HomeModel
import com.mairwunnx.projectessentialscore.helpers.MOD_CONFIG_FOLDER
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileNotFoundException
import kotlin.system.measureTimeMillis

@UseExperimental(UnstableDefault::class)
object StorageBase {
    private val logger = LogManager.getLogger()
    private val userHomeData = hashMapOf<String, HomeModel>()
    private val userDataFolder = MOD_CONFIG_FOLDER + File.separator + "user-data"

    fun getData(uuid: String): HomeModel {
        if (userHomeData.containsKey(uuid)) {
            val requestedData = userHomeData[uuid] ?: HomeModel()
            logger.debug("Requested home data ($requestedData) for UUID ($uuid).")
            return requestedData
        }
        logger.debug("Requested home data not found for UUID ($uuid), will be used default home data.")
        return HomeModel()
    }

    fun setData(uuid: String, data: HomeModel) {
        userHomeData[uuid] = data
        logger.debug("Installed home data (${data}) for UUID ($uuid).")
    }

    fun loadUserData() {
        logger.info("    - loading user home data configuration ...")

        createConfigDirs(userDataFolder)
        val users = File(userDataFolder).list()?.filter {
            if (File(it).isFile) return@filter false
            return@filter true
        }

        val elapsedTime = measureTimeMillis {
            users?.forEach {
                logger.debug("        - processing $it user home data ...")
                val userId = it
                try {
                    val userDataRaw = File(
                        userDataFolder + File.separator + it + File.separator + "home.json"
                    ).readText()
                    val userHomeClass = Json.parse(HomeModel.serializer(), userDataRaw)
                    userHomeData[userId] = userHomeClass
                } catch (_: FileNotFoundException) {
                    logger.info("        - loading home data for $it skipped! not found!")
                }
            }
        }
        logger.info("Loading user home data done configurations with ${elapsedTime}ms")
    }

    fun saveUserData() {
        createConfigDirs(userDataFolder)
        userHomeData.keys.forEach {
            logger.debug("        - processing $it user home data ...")

            val userId = it
            val userDataClass = userHomeData[userId]!!
            val dataFolder = userDataFolder + File.separator + userId
            val dataPath = dataFolder + File.separator + "home.json"

            createConfigDirs(dataFolder)
            logger.debug("        - setup json configuration for parsing ...")
            val json = Json(
                JsonConfiguration(
                    encodeDefaults = true,
                    strictMode = true,
                    unquoted = false,
                    allowStructuredMapKeys = true,
                    prettyPrint = true,
                    useArrayPolymorphism = false
                )
            )
            val userDataRaw = json.stringify(HomeModel.serializer(), userDataClass)
            try {
                File(dataPath).writeText(userDataRaw)
            } catch (ex: SecurityException) {
                logger.error("An error occurred while saving home configuration", ex)
            }
        }
    }

    private fun createConfigDirs(path: String) {
        logger.info("        - creating config directory for user home data ($path)")
        val configDirectory = File(path)
        if (!configDirectory.exists()) configDirectory.mkdirs()
    }
}
