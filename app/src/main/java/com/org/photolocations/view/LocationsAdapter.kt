package com.org.photolocations.view


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.org.photolocations.R
import com.org.photolocations.data.model.DBLocation
import kotlinx.android.synthetic.main.item_location_view.view.*


class LocationsAdapter(private val mListener: OnInteractionListener?)
    : RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private var mValues: MutableList<DBLocation> = ArrayList()

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as DBLocation
            mListener?.onFragmentItemSelected(DBLocation(
                    id = item.id,
                    name = item.name,
                    latitude = item.latitude,
                    longitude = item.longitude,
                    notes = item.notes))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_location_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mDistance.text = "Distance to location is: ${String.format("%.3f km", item.distance)}"
        holder.name.text = item.name
        holder.mLocations.text = "${item.latitude}, ${item.longitude}"

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    fun addAll(collection: Collection<DBLocation>) {
        clear()
        mValues.addAll(collection)
        notifyDataSetChanged()
    }

    private fun clear() {
        mValues.clear()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val name: TextView = mView.name
        val mDistance: TextView = mView.distance
        val mLocations: TextView = mView.location_latitude_longitude
    }
}