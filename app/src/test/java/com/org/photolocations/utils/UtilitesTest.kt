package com.org.photolocations.utils

import com.google.android.gms.maps.model.LatLng
import org.junit.Test

import org.junit.Assert.*

class UtilitesTest {

    @Test
    fun calcDistMeters() {
        val latitude = 15.355547
        val longitude = 15.355547
        val pointToTest = LatLng(latitude, longitude)

        val someLatitude = 15.355547
        val someLongitude = 15.355547
        val somePointToTest = LatLng(someLatitude, someLongitude)
        val answer = calcDistKm(pointToTest, somePointToTest)
        assertEquals(0.0f, answer)
    }

    @Test
    fun calcDistKm() {
        val latitude = 15.355547
        val longitude = 15.355547
        val pointToTest = LatLng(latitude, longitude)

        val someLatitude = 15.355547
        val someLongitude = 15.355547
        val somePointToTest = LatLng(someLatitude, someLongitude)
        val answer = calcDistKm(pointToTest, somePointToTest)
        assertEquals(0.0f, answer)
    }
}