package com.org.photolocations

import android.app.Application
import com.org.photolocations.data.db.RealmMigrations
import io.realm.Realm
import io.realm.RealmConfiguration

class MainApp : Application() {

    companion object {
        lateinit var instance: MainApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // The default Realm file is "default.realm" in Context.getFilesDir();
        Realm.init(this)
        val configRealm = RealmConfiguration.Builder()
                .name("locations.realm")
                .schemaVersion(2)
                .migration(RealmMigrations())
                .build()
        Realm.setDefaultConfiguration(configRealm)
    }

    override fun onTerminate() {
        Realm.getDefaultInstance().close()
        super.onTerminate()
    }

}