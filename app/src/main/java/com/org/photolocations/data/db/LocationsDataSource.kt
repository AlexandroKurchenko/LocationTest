package com.org.photolocations.data.db

import com.org.photolocations.data.model.DBLocation
import io.realm.Realm
import io.realm.RealmResults

class LocationsDataSource(private val realm: Realm) {

    fun createOrUpdateCloudLocations(locations: DBLocation) {
        if (checkLocation(locations)) {
            realm.insertOrUpdate(locations)
        }
    }

    fun getAllDataBaseLocations(): RealmResults<DBLocation>? {
        return realm.where(DBLocation::class.java).findAll()
    }

    fun checkLocation(locations: DBLocation): Boolean {
        val item = realm.where(DBLocation::class.java)
                .equalTo(DBLocation.NAME_KEY, locations.name)
                .and()
                .equalTo(DBLocation.LAT_KEY, locations.latitude)
                .and()
                .equalTo(DBLocation.LNG_KEY, locations.longitude)
                .and()
                .equalTo(DBLocation.NOTES_KEY,locations.notes)
                .count()
        return item <= 0
    }
}