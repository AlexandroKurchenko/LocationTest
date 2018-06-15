package com.org.photolocations.utils

import com.org.photolocations.data.model.DBLocation

/**
 * Helper methods for Unit testing
 */
class UnitTestingUtils {
    companion object {
        val TEST_LOCATION_NAME: String = "Test location"
        val TEST_LOCATION_ID: String = "testlocation111111id"
        val TEST_LOCATION_LATITUDE: Double = 34.111
        val TEST_LOCATION_LONGITUDE: Double = 45.123
        val TEST_LOCATION_NOTES: String = "Nice spot"

        fun createTestLocation(): DBLocation {
            return DBLocation(TEST_LOCATION_ID,
                    TEST_LOCATION_NAME,
                    TEST_LOCATION_LATITUDE,
                    TEST_LOCATION_LONGITUDE,
                    TEST_LOCATION_NOTES)
        }

        fun createTestListOfLocations(numberOfItems: Int): ArrayList<DBLocation> {
            val list: ArrayList<DBLocation> = ArrayList<DBLocation>()
            repeat(numberOfItems, action = {list.add(DBLocation())})
            return list
        }
    }
}