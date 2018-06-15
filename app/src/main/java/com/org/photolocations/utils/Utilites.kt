package com.org.photolocations.utils

import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng

/**
 *Constant TAG name for Map fragment @see MapFragment.kt
 */
const val MAP_FRAGMENT_TAG = "map"
/**
 *Constant TAG name for List fragment @see ListFragment.kt
 */
 const val LIST_FRAGMENT_TAG = "list"

/**
 * Function for wrapping objects for distance calculation
 *
 * @param beginLatLng - starting point from where need to calculate
 * @param finalLatLng - final point from where need to calculate
 */
private fun distWrapper(beginLatLng: LatLng, finalLatLng: LatLng): Pair<Location, Location> {
    val beginLocation = Location("beginLatLng")
    beginLocation.latitude = beginLatLng.latitude
    beginLocation.longitude = beginLatLng.longitude

    val finalLocation = Location("finalLatLng")
    finalLocation.latitude = finalLatLng.latitude
    finalLocation.longitude = finalLatLng.longitude
    return Pair(beginLocation, finalLocation)
}

/**
 * Calculates distance in meters
 *
 * @param beginLatLng - starting point from where need to calculate
 * @param finalLatLng - final point from where need to calculate
 */
fun calcDistMeters(beginLatLng: LatLng, finalLatLng: LatLng): Float {
    val distWrapper = distWrapper(beginLatLng, finalLatLng)
    val distance = distWrapper.first.distanceTo(distWrapper.second)
    return distance
}

/**
 * Calculates distance in kilometers
 *
 * @param beginLatLng - starting point from where need to calculate
 * @param finalLatLng - final point from where need to calculate
 */
fun calcDistKm(beginLatLng: LatLng, finalLatLng: LatLng): Float {
    val distWrapper = distWrapper(beginLatLng, finalLatLng)
    val distance = distWrapper.first.distanceTo(distWrapper.second) / 1000
    return distance

}