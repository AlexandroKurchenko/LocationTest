package com.org.photolocations.data.model

/**
 * Model of Cloud retrieved data
 * which can be viewed at @see SERVER_URL at CloudManager
 */

data class CloudLocations(
        val locations: List<DBLocation>,
        val updated: String // 2016-12-01T06:52:08Z
)