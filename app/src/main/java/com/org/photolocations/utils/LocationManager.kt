package com.org.photolocations.utils

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

/**
 * Used for getting location, needed to calculate distance to photo location
 * for any questions @see "https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient"
 */

class LocationManager(private val context: FragmentActivity, private val listener: LocationCompleteListener) {

    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var lastLocation: LatLng? = null

    fun retrieveLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.lastLocation
                    .addOnCompleteListener(context) { task ->
                        if (task.isSuccessful && task.result != null) {
                            lastLocation = LatLng(task.result.latitude, task.result.longitude)
//                            lastLocation = LatLng(-33.889961, 151.27644) // test location for minimize distance
                        } else {
                            requestLocation()
                        }
                        listener.successLocationRetrieve()
                    }
        }
    }

    /**
     * Return current location which will be retrieved from FusedLocationProviderClient
     */
    fun getLocationLast(): LatLng? {
        return lastLocation
    }

    /**
     * Start requesting update for location in case when lastLocation is null or empty
     */
    private fun requestLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val mLocationRequest = LocationRequest.create()
            mLocationRequest.interval = 60000
            mLocationRequest.fastestInterval = 5000
            mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        }
    }

    private var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            if (locationResult == null) {
                return
            }
            for (location in locationResult.locations) {
                if (location != null) {
                    lastLocation = LatLng(location.latitude, location.longitude)
                    removeLocationUpdate()
                    listener.successLocationRetrieve()
                }
            }
        }
    }

    /**
     * Remove location update listener
     */
    private fun removeLocationUpdate() {
        fusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

}