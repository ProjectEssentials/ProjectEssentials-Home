package com.mairwunnx.projectessentials.home.configurations

import com.mairwunnx.projectessentials.home.enums.HomeSelectStrategy
import kotlinx.serialization.Serializable

@Serializable
data class HomeSettingsConfigurationModel(
    var respawnAtHomeAfterDeath: Boolean = true,
    var respawnHomeSelectStrategy: HomeSelectStrategy = HomeSelectStrategy.Last,
    var playSoundOnTeleport: Boolean = false,
    var showEffectOnTeleport: Boolean = false,
    var homeLimitations: Map<String, String> = mapOf("default" to "6")
)
