package com.org.photolocations.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.org.photolocations.R
import com.org.photolocations.data.model.DBLocation
import com.org.photolocations.view.presenters.ListViewPresenter
import org.jetbrains.annotations.NotNull


/**
 * A fragment representing a list of Items.
 */
class ListFragment : Fragment() {

    private val PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 456
    private lateinit var adapter: LocationsAdapter
    private var listener: OnInteractionListener? = null
    private val presenter = ListViewPresenter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter.onViewAttached(this)
        if (context is OnInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnInteractionListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false) as RecyclerView
        view.layoutManager = LinearLayoutManager(context)
        adapter = LocationsAdapter(listener)
        view.adapter = adapter

        requestPermission()
        return view
    }

    override fun onDetach() {
        super.onDetach()
        presenter.onViewDetach()
        listener = null
    }

    private fun requestPermission() {
        activity?.let {
            if (ContextCompat.checkSelfPermission(it,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    Snackbar.make(it.findViewById(android.R.id.content), R.string.location_permission_msg,
                            Snackbar.LENGTH_INDEFINITE).setAction("OK") {
                        // Request the permission
                        requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                                PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION)
                    }.show()

                } else {
                    // No explanation needed, we can request the permission.
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                            PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION)

                    // PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
                presenter.retrieveLocation()

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted
                    presenter.retrieveLocation()
                } else {
                    // permission denied
                    Snackbar.make(activity!!.findViewById(android.R.id.content), R.string.location_disallowed, Snackbar.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    /**
     * Update list adapter, with items which was retrieved from database
     */
    fun updateAdapter(@NotNull locationList: List<DBLocation>) {
        adapter.addAll(locationList)
    }

}