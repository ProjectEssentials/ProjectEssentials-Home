package com.mairwunnx.projectessentials.home

import com.mairwunnx.projectessentials.home.models.HomeModel
import com.mairwunnx.projectessentials.home.storage.StorageBase
import net.minecraft.entity.player.ServerPlayerEntity

/**
 * Home API base class. Contains all
 * methods for interaction with player homes.
 *
 * @since 1.14.4-1.2.0
 */
object HomeAPI {
    /**
     * Create home for target player with specified name.
     *
     * @param owner ServerPlayerEntity class instance,
     * target home owner.
     * @param name new home name with default value `home`.
     * @param override if true then if world existing with same
     * name then it world will be replaced with new home record,
     * default value is `false`.
     *
     * @return true if home creating successful otherwise false.
     *
     * @since 1.14.4-1.2.0
     */
    fun create(
        owner: ServerPlayerEntity,
        name: String = "home",
        override: Boolean = false
    ): Boolean {
        val playerUUID = owner.uniqueID.toString()
        val clientWorld = owner.commandSource.world.worldInfo.worldName
        val worldId = owner.commandSource.world.worldType.id
        val xPos = owner.posX.toInt()
        val yPos = owner.posY.toInt()
        val zPos = owner.posZ.toInt()
        val yaw = owner.rotationYaw
        val pitch = owner.rotationPitch
        val homes = takeAll(owner)

        if (override) {
            homes.removeAll {
                it.home == name
            }
        } else {
            if (contains(
                    homes,
                    name
                )
            ) return false
        }

        homes.add(
            HomeModel.Home(
                name, clientWorld, worldId, xPos, yPos, zPos, yaw, pitch
            )
        )
        StorageBase.setData(playerUUID, HomeModel(homes))
        return true
    }

    /**
     * Remove home for target player with specified name.
     *
     * @param owner ServerPlayerEntity class instance,
     * target home owner.
     * @param name home name to remove with default value `home`.
     *
     * @return true if home removing successful otherwise false.
     *
     * @since 1.14.4-1.2.0
     */
    fun remove(
        owner: ServerPlayerEntity,
        name: String = "home"
    ): Boolean {
        val playerUUID = owner.uniqueID.toString()
        val homes = takeAll(owner)

        take(owner, name)?.let {
            homes.remove(it)
            StorageBase.setData(playerUUID, HomeModel(homes))
            return true
        }
        return false
    }

    /**
     * @param owner ServerPlayerEntity class instance,
     * target home owner.
     *
     * @return all player registered homes.
     *
     * @since 1.14.4-1.2.0
     */
    fun takeAll(owner: ServerPlayerEntity): MutableList<HomeModel.Home> =
        StorageBase.getData(owner.uniqueID.toString()).homes

    /**
     * @param owner ServerPlayerEntity class instance,
     * target home owner.
     * @param name home name with default value `home`.
     *
     * @return null if home not exist otherwise home instance.
     *
     * @since 1.14.4-1.2.0
     */
    fun take(
        owner: ServerPlayerEntity,
        name: String = "home"
    ): HomeModel.Home? {
        val homes = takeAll(owner)
        homes.forEach {
            if (it.home == name) return it
        }
        return null
    }

    /**
     * @param homeCollection player home collection. Can
     * be taken with `takeAll` method.
     * @param name home name.
     *
     * @return true if home exist otherwise false.
     *
     * @see takeAll
     *
     * @since 1.14.4-1.2.0
     */
    fun contains(
        homeCollection: List<HomeModel.Home>,
        name: String
    ): Boolean {
        homeCollection.forEach {
            if (it.home == name) return true
        }
        return false
    }

    /**
     * Saves user data to local storage, to json file.
     */
    fun save() = StorageBase.saveUserData()

    /**
     * Reloading configuration from local storage,
     * with saving if argument `withSaving` true.
     *
     * @param withSaving if true then configuration
     * will be saved before reloading. Default values
     * is true.
     */
    fun reload(withSaving: Boolean = true) {
        if (withSaving) save()
        StorageBase.loadUserData()
    }
}
