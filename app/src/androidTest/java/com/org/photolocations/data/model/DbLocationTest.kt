package com.org.photolocations.data.model

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.org.photolocations.utils.UnitTestingUtils
import io.realm.Realm
import io.realm.RealmConfiguration
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DbLocationTest {

    private lateinit var realm: Realm

    @Before fun setUp() {
        Realm.init(InstrumentationRegistry.getContext().applicationContext)
        val config = RealmConfiguration.Builder().inMemory().name("test-realm").build()
        realm = Realm.getInstance(config)

    }

    @After
    fun tearDown() {
        realm.close()
    }

    @Test
    fun saveDbLocation() {

        val location: DBLocation = UnitTestingUtils.createTestLocation()
        realm.executeTransaction {
            it.copyToRealmOrUpdate(location)
        }
        val retreivedLocation: DBLocation? =
                realm.where(DBLocation::class.java)
                        .equalTo(DBLocation.ID_KEY, UnitTestingUtils.TEST_LOCATION_ID)
                        .findFirst()
        Assert.assertTrue(retreivedLocation != null)
        Assert.assertEquals("Compare locations ids", location.id, retreivedLocation?.id)
        Assert.assertEquals("Compare locations names", location.name, retreivedLocation?.name)
        Assert.assertEquals("Compare locations notes", location.notes, retreivedLocation?.notes)
        Assert.assertEquals("Compare locations latitude", location.latitude, retreivedLocation?.latitude)
        Assert.assertEquals("Compare locations longitude", location.longitude, retreivedLocation?.longitude)
    }

    @Test
        fun modifyingObjectFromDb() {

    }
}