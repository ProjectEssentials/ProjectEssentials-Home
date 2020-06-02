package com.mairwunnx.projectessentials.home.configurations

import kotlinx.serialization.Serializable

@Serializable
data class HomeConfigurationModel(
    val users: MutableList<User> = mutableListOf()
) {
    @Serializable
    data class User(
        var name: String,
        var uuid: String,
        val homes: MutableList<Home>
    ) {
        @Serializable
        data class Home(
            var home: String,
            var worldId: Int,
            var xPos: Int,
            var yPos: Int,
            var zPos: Int,
            var yaw: Float,
            var pitch: Float
        )
    }
}
