package com.org.photolocations.view.presenters

import com.org.photolocations.data.model.DBLocation

interface IMainActivityPresenter<IMainActivityView> : IBasePresenter<IMainActivityView> {
    fun onLocationSelected(location: DBLocation)
    fun onLocationToSave(name: String, notes: String)
}