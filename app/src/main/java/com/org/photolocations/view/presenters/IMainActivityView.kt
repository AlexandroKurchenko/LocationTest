package com.org.photolocations.view.presenters

import com.org.photolocations.data.model.DBLocation


interface IMainActivityView {
    fun onDisplayLocation(location: DBLocation)
    fun onLocationSaved()
    fun onLocationSaveFailed()
}