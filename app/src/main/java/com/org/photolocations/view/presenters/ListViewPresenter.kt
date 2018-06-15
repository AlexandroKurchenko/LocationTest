package com.org.photolocations.view.presenters

import android.support.design.widget.Snackbar
import com.google.android.gms.maps.model.LatLng
import com.org.photolocations.R
import com.org.photolocations.data.db.DataBaseManager
import com.org.photolocations.data.model.DBLocation
import com.org.photolocations.utils.LocationCompleteListener
import com.org.photolocations.utils.LocationManager
import com.org.photolocations.utils.calcDistKm
import com.org.photolocations.view.ListFragment
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults

class ListViewPresenter : IListPresenter<ListFragment>, LocationCompleteListener {

    private lateinit var locationManager: LocationManager
    private var view: ListFragment? = null
    private var presentationDisposable: Disposable? = null

    override fun onViewAttached(view: ListFragment) {
        this.view = view
        Realm.getDefaultInstance().addChangeListener(realmChangeListener)
        locationManager = LocationManager(view.activity!!, this)
    }

    override fun onViewDetach() {
        presentationDisposable?.dispose()
        Realm.getDefaultInstance().removeChangeListener(realmChangeListener)
        Realm.getDefaultInstance().close()
    }

    override fun successLocationRetrieve() {
        getLocationData()
    }

    private val realmChangeListener = RealmChangeListener<Realm> { it ->
        val results = it.where(DBLocation::class.java)
                .findAll()
        processLocationData(results)
    }

    private fun getLocationData() {
        val results = DataBaseManager().getDBLocations()
        processLocationData(results)
    }

    fun retrieveLocation() {
        locationManager.retrieveLocation()
    }

    private fun processLocationData(results: RealmResults<DBLocation>?) {
        if (results == null) {
            return
        }
        if (results.isValid && results.isNotEmpty()) {
            val convertedResult = Realm.getDefaultInstance().copyFromRealm(results)
            val lastKnownLocation = locationManager.getLocationLast()

            lastKnownLocation?.let {
                Observable.just(convertedResult) // extension function for Iterables
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter { list -> !list.isEmpty() }
                        .map {
                            it.sortedWith(compareBy {
                                val distanceCalc = calcDistKm(lastKnownLocation, LatLng(it.latitude, it.longitude))
                                it.distance = distanceCalc
                                distanceCalc
                            })
                        }
                        .subscribe(object : Observer<List<DBLocation>> {
                            override fun onComplete() {}

                            override fun onSubscribe(disposable: Disposable) {
                                presentationDisposable = disposable
                            }

                            override fun onNext(finalResults: List<DBLocation>) {
                                view?.updateAdapter(finalResults)
                            }

                            override fun onError(e: Throwable) {
                                e.printStackTrace()
                            }
                        })
            } ?: kotlin.run {
                view?.updateAdapter(results)
                view?.activity?.let {
                    Snackbar.make(it.findViewById(android.R.id.content), R.string.location_disallowed, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}