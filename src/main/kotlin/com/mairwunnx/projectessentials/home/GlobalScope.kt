package com.mairwunnx.projectessentials.home

import com.mairwunnx.projectessentials.core.api.v1.configuration.ConfigurationAPI
import com.mairwunnx.projectessentials.core.api.v1.extensions.playSound
import com.mairwunnx.projectessentials.home.configurations.HomeConfiguration
import com.mairwunnx.projectessentials.home.configurations.HomeConfigurationModel
import com.mairwunnx.projectessentials.home.configurations.HomeSettingsConfiguration
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.particles.ParticleTypes
import net.minecraft.util.SoundEvents
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import kotlin.random.Random

val homeConfiguration by lazy {
    ConfigurationAPI.getConfigurationByName<HomeConfiguration>("home").take()
}

val homeSettingsConfiguration by lazy {
    ConfigurationAPI.getConfigurationByName<HomeSettingsConfiguration>("home-settings").take()
}

fun teleportToHome(player: ServerPlayerEntity, home: HomeConfigurationModel.User.Home) {
    val world = player.server.getWorld(
        DimensionType.getById(home.dimensionId) ?: DimensionType.OVERWORLD
    )
    player.teleport(world, home.xPos + 0.5, home.yPos + 0.5, home.zPos + 0.5, home.yaw, home.pitch)
    if (homeSettingsConfiguration.playSoundOnTeleport) {
        player.playSound(player, SoundEvents.ENTITY_ENDERMAN_TELEPORT)
    }
    if (homeSettingsConfiguration.showEffectOnTeleport) {
        DistExecutor.runWhenOn(Dist.CLIENT) {
            Runnable {
                for (i in 0..200) {
                    Minecraft.getInstance().world?.addParticle(
                        ParticleTypes.PORTAL,
                        player.positionVec.x + (random.nextDouble() - 0.5) * player.width.toDouble(),
                        player.positionVec.y + random.nextDouble() * player.height.toDouble() - 0.25,
                        player.positionVec.z + (random.nextDouble() - 0.5) * player.width.toDouble(),
                        (random.nextDouble() - 0.5) * 2.0,
                        -random.nextDouble(),
                        (random.nextDouble() - 0.5) * 2.0
                    )
                }
                if (player.world.isRemote) spawnServerParticles(player)
            }
        }
        DistExecutor.runWhenOn(Dist.DEDICATED_SERVER) { Runnable { spawnServerParticles(player) } }
    }
}

private val random = Random
private fun spawnServerParticles(player: ServerPlayerEntity) {
    for (i in 0..200) {
        player.serverWorld.spawnParticle(
            ParticleTypes.PORTAL,
            player.posX + (random.nextDouble() - 0.5) * player.width.toDouble(),
            player.posY + random.nextDouble() * player.height.toDouble() - 0.25,
            player.posZ + (random.nextDouble() - 0.5) * player.width.toDouble(),
            1,
            -0.006, -0.006, 0.0,
            (random.nextDouble() - 0.5) * 2.0
        )
    }
}
