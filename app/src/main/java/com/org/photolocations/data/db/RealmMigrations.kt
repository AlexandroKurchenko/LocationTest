package com.org.photolocations.data.db

import io.realm.DynamicRealm
import io.realm.RealmMigration

/**
 * Migration schema for Realm database
 */
class RealmMigrations : RealmMigration {

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema

        if (oldVersion == 1L) {
            val userSchema = schema.get("DBLocation")
            userSchema!!.addField("distance", Float::class.javaPrimitiveType)
        }
    }
}