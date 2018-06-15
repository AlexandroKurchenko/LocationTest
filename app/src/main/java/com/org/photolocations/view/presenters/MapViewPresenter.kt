package com.org.photolocations.view.presenters

import android.support.design.widget.Snackbar
import com.org.photolocations.R
import com.org.photolocations.data.cloud.CloudManager
import com.org.photolocations.data.cloud.ICloudRequest
import com.org.photolocations.data.db.DataBaseManager
import com.org.photolocations.data.model.CloudLocations
import com.org.photolocations.data.model.DBLocation
import com.org.photolocations.view.MapFragment
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults

class MapViewPresenter : IMapPresenter<MapFragment>, ICloudRequest {

    private var view: MapFragment? = null
    private var dbManager = DataBaseManager()

    override fun onViewAttached(view: MapFragment) {
        this.view = view
        Realm.getDefaultInstance().addChangeListener(realmChangeListener)
    }

    override fun onViewDetach() {
        this.view = null
        Realm.getDefaultInstance().removeChangeListener(realmChangeListener)
        Realm.getDefaultInstance().close()
    }

    override fun success(cloudLocations: CloudLocations) {
        saveCloudLocations(cloudLocations)
    }

    override fun error(errorMessage: String?) {
        view?.activity?.let {
            Snackbar.make(it.findViewById(android.R.id.content), R.string.error_cloud_data, Snackbar.LENGTH_LONG).show()
        }
    }

    private val realmChangeListener = RealmChangeListener<Realm> { it ->
        val results = it.where(DBLocation::class.java)
                .findAll()
        showLocationInMarkers(results)
    }

    private fun processLoadCloudData() {
        CloudManager(this).getCloudData()
    }

    private fun showLocationInMarkers(results: RealmResults<out DBLocation>?) {
        results?.let {
            if (results.isValid && results.isNotEmpty()) {
                Realm.getDefaultInstance().copyFromRealm(results)?.let {
                    view?.showLocationMarkers(it)
                }
            }
        }
    }

    fun loadLocationItems() {
        dbManager.getDBLocations()?.let {
            if (it.isValid && it.isNotEmpty()) {
                showLocationInMarkers(it)
            } else {
                processLoadCloudData()
            }
        }
    }

    private fun saveCloudLocations(cloudLocations: CloudLocations) {
        dbManager.processCloudLocations(cloudLocations)
    }
}