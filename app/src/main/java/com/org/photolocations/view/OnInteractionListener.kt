package com.org.photolocations.view

import com.org.photolocations.data.model.DBLocation
import org.jetbrains.annotations.NotNull

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 */
interface OnInteractionListener {

    fun onFragmentItemSelected(@NotNull item: DBLocation)
}