package com.mairwunnx.projectessentials.home.api

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
        val homes = StorageBase.getData(playerUUID).homes

        if (homes.isNotEmpty()) {
            if (override) {
                homes.removeAll {
                    it.home == name
                }
            } else {
                homes.forEach {
                    if (it.home == name) return false
                }
            }
        }

        homes.add(
            HomeModel.Home(
                name, clientWorld, worldId, xPos, yPos, zPos, yaw, pitch
            )
        )
        StorageBase.setData(playerUUID, HomeModel(homes))
        return true
    }
}
