package com.org.photolocations.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.org.photolocations.data.model.DBLocation
import com.org.photolocations.view.presenters.MapViewPresenter
import org.jetbrains.annotations.NotNull
import java.util.*


class MapFragment : SupportMapFragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var listener: OnInteractionListener? = null
    private val presenter = MapViewPresenter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnInteractionListener")
        }
        presenter.onViewAttached(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMapAsync(this)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        presenter.onViewDetach()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Enabling MyLocation Layer of Google Map
        if (activity != null && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        }
        mMap.setOnMarkerClickListener { marker ->
            val location = marker.tag as DBLocation
            listener?.onFragmentItemSelected(location)
            true
        }
        mMap.setOnMapLongClickListener {
            listener?.onFragmentItemSelected(DBLocation(
                    name = "",
                    id = UUID.randomUUID().toString(),
                    latitude = it.latitude,
                    longitude = it.longitude))
        }
        presenter.loadLocationItems()
    }

    /**
     *
     * Returns a CameraUpdate that moves the center of the screen to a latitude and longitude specified by a LatLng object, and moves to the given zoom level.
     *
     * the desired zoom level, in the range of 2.0 to 21.0. Values below this range are set to 2.0,
     * and values above it are set to 21.0. Increase the value to zoom in.
     * Not all areas have tiles at the largest zoom levels.
     */
    fun showLocationMarkers(@NotNull items: List<DBLocation>) {
        for (res in items) {
            val markerPosition = LatLng(res.latitude, res.longitude)
            val marker = mMap.addMarker(MarkerOptions().position(markerPosition).title(res.name))
            marker.tag = res
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerPosition, 10f)
            mMap.animateCamera(cameraUpdate)
        }
    }
}
