package com.mairwunnx.projectessentials.home.storage

import com.mairwunnx.projectessentials.core.helpers.MOD_CONFIG_FOLDER
import com.mairwunnx.projectessentials.core.helpers.jsonInstance
import com.mairwunnx.projectessentials.home.models.HomeModel
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileNotFoundException
import kotlin.system.measureTimeMillis

internal object StorageBase {
    private val logger = LogManager.getLogger()
    private val userHomeData = hashMapOf<String, HomeModel>()
    private val userDataFolder = MOD_CONFIG_FOLDER + File.separator + "user-data"

    internal fun getData(uuid: String): HomeModel {
        if (userHomeData.containsKey(uuid)) {
            val requestedData = userHomeData[uuid] ?: HomeModel()
            logger.debug("Requested home data ($requestedData) for UUID ($uuid).")
            return requestedData
        }
        logger.debug("Requested home data not found for UUID ($uuid), will be used default home data.")
        return HomeModel()
    }

    internal fun setData(uuid: String, data: HomeModel) {
        userHomeData[uuid] = data
        logger.debug("Installed home data (${data}) for UUID ($uuid).")
    }

    internal fun loadUserData() {
        logger.info("Loading user home data configuration")

        File(userDataFolder).mkdirs()
        val users = File(userDataFolder).list()?.filter {
            if (File(it).isFile) return@filter false
            return@filter true
        }

        val elapsedTime = measureTimeMillis {
            users?.forEach {
                val userId = it
                try {
                    val userDataRaw = File(
                        userDataFolder + File.separator + it + File.separator + "home.json"
                    ).readText()
                    val userHomeClass = jsonInstance.parse(
                        HomeModel.serializer(), userDataRaw
                    )
                    userHomeData[userId] = userHomeClass
                } catch (_: FileNotFoundException) {
                    logger.info("Loading home data for $it skipped! not found!")
                }
            }
        }
        logger.info("Loading user home data done configurations with ${elapsedTime}ms")
    }

    internal fun saveUserData() {
        File(userDataFolder).mkdirs()
        userHomeData.keys.forEach {
            val userId = it
            val userDataClass = userHomeData[userId]!!
            val dataFolder = userDataFolder + File.separator + userId
            val dataPath = dataFolder + File.separator + "home.json"

            File(dataFolder).mkdirs()
            val userDataRaw = jsonInstance.stringify(HomeModel.serializer(), userDataClass)
            try {
                File(dataPath).writeText(userDataRaw)
            } catch (ex: SecurityException) {
                logger.error("An error occurred while saving home configuration", ex)
            }
        }
    }
}
