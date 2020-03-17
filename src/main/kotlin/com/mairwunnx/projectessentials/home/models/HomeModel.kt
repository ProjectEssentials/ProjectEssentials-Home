package com.mairwunnx.projectessentials.home.models

import com.mairwunnx.projectessentials.core.extensions.empty
import kotlinx.serialization.Serializable

/**
 * Home model, contains all player homes.
 */
@Serializable
data class HomeModel(
    /**
     * Player homes collection, represent as
     * mutable list with type `Home` class.
     */
    var homes: MutableList<Home> = mutableListOf()
) {
    /**
     * Home class, contains needed properties
     * for indexing home and teleporting to home.
     */
    @Serializable
    data class Home(
        /**
         * Home name, default value is empty string.
         */
        var home: String = String.empty,
        /**
         * Client world represent as string. Default
         * value is empty string.
         *
         * In this world contains this home.
         */
        var clientWorld: String = String.empty,
        /**
         * Server world id. Default value is `-1`.
         *
         * In this world (dimension) contains this home.
         */
        var worldId: Int = -1,
        /**
         * Home position by `x` axis. Default value is `-1`.
         */
        var xPos: Int = -1,
        /**
         * Home position by `y` axis. Default value is `-1`.
         */
        var yPos: Int = -1,
        /**
         * Home position by `z` axis. Default value is `-1`.
         */
        var zPos: Int = -1,
        /**
         * Player camera rotation yaw. Default value is `-1`.
         */
        var yaw: Float = -1F,
        /**
         * Player camera rotation pitch. Default value is `-1`.
         */
        var pitch: Float = -1F
    )
}
