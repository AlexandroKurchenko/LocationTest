package com.org.photolocations.data.db

import com.org.photolocations.data.model.CloudLocations
import com.org.photolocations.data.model.DBLocation
import io.realm.Realm
import io.realm.RealmResults
import org.jetbrains.annotations.NotNull

class DataBaseManager {

    fun processCloudLocations(@NotNull cloudLocations: CloudLocations) {
        cloudLocations.locations.let {
            if (it.isNotEmpty()) {
                // iterate and put cloud locations to dataBase
                for (location in it) {
                    addCloudLocations(location)
                }
            }
        }
    }

    private fun addCloudLocations(@NotNull location: DBLocation) {
        val realm: Realm = Realm.getDefaultInstance()
        realm.executeTransaction({
            LocationsDataSource(it).createOrUpdateCloudLocations(location)
        })
        realm.close()
    }

    fun getDBLocations(): RealmResults<DBLocation>? {
        val realm: Realm = Realm.getDefaultInstance()
        var results: RealmResults<DBLocation>? = null
        realm.executeTransaction({
            results = LocationsDataSource(realm).getAllDataBaseLocations()
        })
        realm.close()
        return results
    }

    fun addOrUpdateLocation(@NotNull location: DBLocation) {
        val realm: Realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.copyToRealmOrUpdate(location)
        }
        realm.close()
    }
}