package com.org.photolocations.view

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.org.photolocations.MainApp
import com.org.photolocations.R
import com.org.photolocations.data.db.DataBaseManager
import com.org.photolocations.data.model.DBLocation
import com.org.photolocations.view.presenters.IMainActivityPresenter
import com.org.photolocations.view.presenters.IMainActivityView
import com.org.photolocations.view.presenters.MainActivityPresenter
import com.org.photolocations.utils.LIST_FRAGMENT_TAG
import com.org.photolocations.utils.MAP_FRAGMENT_TAG
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.edit_view.*
import org.jetbrains.annotations.NotNull


class MainActivity : AppCompatActivity(), OnInteractionListener, IMainActivityView {

    private var customDBLocation: DBLocation? = null
    private val presenter = MainActivityPresenter()
    private var sheetBehavior: BottomSheetBehavior<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.onViewAttached(this)
        sheetBehavior = BottomSheetBehavior.from(edit_view)
        sheetBehavior?.setBottomSheetCallback(bottomSheetCallback)
        save_custom_fab.setOnClickListener {
            saveCustomLocationData()
        }

        navigationImage.setOnClickListener {
            fragmentNavigator(it)
        }
        //When start activity show map fragment
        navigateToMapFragment()
        mainToolbar.title = getString(R.string.action_map)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDetach()
    }

    override fun onFragmentItemSelected(item: DBLocation) {
        customDBLocation = item
        presenter.onLocationSelected(item)
    }

    private fun fragmentNavigator(view: View) {
        if (view.tag == getString(R.string.action_list)) {
            navigateToListView()
        } else {
            navigateToMapFragment()
        }
        changeImgProp(view.tag as String)
        hideEditView()
    }

    private fun navigateToMapFragment() {
        val mapFragment = MapFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.map, mapFragment, MAP_FRAGMENT_TAG)
        fragmentTransaction.commit()
    }

    private fun navigateToListView() {
        val mapFragment = ListFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.map, mapFragment, LIST_FRAGMENT_TAG)
        fragmentTransaction.commit()
    }

    private fun changeImgProp(@NotNull tag: String) {
        when (tag) {
            getString(R.string.action_list) -> {
                navigationImage.setImageResource(R.drawable.ic_map)
                navigationImage.tag = getString(R.string.action_map)
                mainToolbar.title = getString(R.string.action_map)
            }
            else -> {
                navigationImage.setImageResource(R.drawable.ic_list)
                navigationImage.tag = getString(R.string.action_list)
                mainToolbar.title = getString(R.string.action_list)

            }

        }
    }

    private fun showEditView() {
        sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun hideEditView() {
        sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun saveCustomLocationData() {
        presenter.onLocationToSave(location_name.text.toString(), location_notes.text.toString())
    }

    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            // this part hides the button immediately and waits bottom sheet
            // to collapse to show
            if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                save_custom_fab.visibility = View.INVISIBLE
                hideKeyboard()
            } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                save_custom_fab.visibility = View.VISIBLE
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(notes?.windowToken, 0)
    }

    override fun onLocationSaved() {
        hideEditView()
    }

    override fun onLocationSaveFailed() {
        Snackbar.make(findViewById(android.R.id.content), "Please enter name first", Snackbar.LENGTH_LONG).show()
    }

    override fun onDisplayLocation(location: DBLocation) {
        location_latitude.text = location.latitude.toString()
        location_longitude.text = location.longitude.toString()
        location_notes.setText(location.notes)
        location_name.setText(location.name)
        showEditView()
    }
}
