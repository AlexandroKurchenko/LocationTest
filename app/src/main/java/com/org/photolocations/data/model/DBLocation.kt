package com.org.photolocations.data.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Data model of location object, which will be stored in Realm DB
 */
open class DBLocation(@PrimaryKey var id: String,
                      @SerializedName("name") var name: String,
                      @SerializedName("lat") var latitude: Double,
                      @SerializedName("lng") var longitude: Double,
                      var notes: String = "",
                      var distance: Float = 0F
) : RealmObject() {
    //Default constructor for realm DB
    constructor() : this(id = UUID.randomUUID().toString(), name = "", latitude = 0.0, longitude = 0.0)

    companion object {
        const val ID_KEY = "id"
        const val NAME_KEY = "name"
        const val LAT_KEY = "latitude"
        const val LNG_KEY = "longitude"
        const val NOTES_KEY = "notes"
    }

    override fun toString(): String {
        return "DBLocation(id='$id', name='$name', latitude=$latitude, longitude=$longitude, notes='$notes', distance='$distance')"
    }


}