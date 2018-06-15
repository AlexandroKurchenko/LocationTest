package com.org.photolocations.data.db

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.org.photolocations.data.model.DBLocation
import com.org.photolocations.data.model.DbLocationTest
import com.org.photolocations.utils.UnitTestingUtils
import io.realm.Realm
import io.realm.RealmConfiguration
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LocationDataSourceTest {
    private lateinit var realm: Realm

    @Before
    fun setUp() {
        Realm.init(InstrumentationRegistry.getContext().applicationContext)
        val config = RealmConfiguration.Builder().inMemory().name("test-realm").build()
        realm = Realm.getInstance(config)

    }

    @After
    fun tearDown() {
        realm.close()
    }
    @Test fun notAllowingToWriteAlreadyStoredLocation() {
        val testLocation: DBLocation = UnitTestingUtils.createTestLocation()
        realm.executeTransaction {
            it.copyToRealmOrUpdate(testLocation)
        }
        val locationDataSource = LocationsDataSource(realm)
        Assert.assertFalse(locationDataSource.checkLocation(testLocation))
    }

    @Test fun allowingToWriteNewLocation() {
        val testLocation: DBLocation = UnitTestingUtils.createTestLocation()
        val locationDataSource = LocationsDataSource(realm)
        Assert.assertTrue(locationDataSource.checkLocation(testLocation))
    }

    @Test fun allLocationsWritten() {
        val numberOfLOcations = 5
        val locations = UnitTestingUtils.createTestListOfLocations(numberOfLOcations)
        val locationDataSource = LocationsDataSource(realm)
        for (location in locations) {
            realm.executeTransaction {
                it.copyToRealmOrUpdate(location)
            }
        }
        Assert.assertEquals("Number of locations", numberOfLOcations,
                locationDataSource.getAllDataBaseLocations()?.size)
    }
}