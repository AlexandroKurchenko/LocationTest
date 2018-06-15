package com.org.photolocations.view.presenters

import android.util.Log
import com.org.photolocations.data.db.DataBaseManager
import com.org.photolocations.data.model.DBLocation
import com.org.photolocations.view.MainActivity


class MainActivityPresenter : IMainActivityPresenter<IMainActivityView> {
    private var mainActivity: IMainActivityView? = null
    private lateinit  var selectedLocation : DBLocation

    override fun onLocationSelected(location: DBLocation) {
        selectedLocation = location
        mainActivity?.onDisplayLocation(selectedLocation)
    }

    override fun onLocationToSave(name: String, notes: String) {
        Log.d("TRACE", "name:$name nortes:$notes")
        if (name.isEmpty()) {
            mainActivity?.onLocationSaveFailed()
            return
        }
        selectedLocation.name = name
        selectedLocation.notes = notes
        DataBaseManager().addOrUpdateLocation(selectedLocation)
        mainActivity?.onLocationSaved()
    }

    override fun onViewAttached(view: IMainActivityView) {
        mainActivity = view
    }

    override fun onViewDetach() {
        mainActivity = null
    }
}