package com.org.photolocations.data.cloud

import com.google.gson.Gson
import com.org.photolocations.data.model.CloudLocations
import okhttp3.*
import java.io.IOException

// Source link for data which was retrieved from cloud
const val SERVER_URL = "https://s3-ap-southeast-2.amazonaws.com/com-cochlear-sabretooth-takehometest/locations.json"

/**
 * CloudManager is class for processing cloud data via Okhttp library
 */
class CloudManager(private val dataListener: ICloudRequest) {

    /**
     * Call this functionality for get cloud items of location
     * @see SERVER_URL
     *
     * Answer of success response will be send via ICloudRequest.success() interface
     */
    fun getCloudData() {
        OkHttpClient().newCall(createRequest()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                dataListener.error(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    dataListener.error("Unexpected code ${response.code()}")
                    return
                }

                val convertedResponse = convertResponse(response)
                dataListener.success(convertedResponse)
            }
        })
    }

    fun createRequest(): Request {
        return Request.Builder()
                .url(SERVER_URL)
                .build()
    }

    /**
     * Converting response from cloud to CloudLocations object
     * and send it in success callback of ICloudRequest interface
     */
    fun convertResponse(response: Response): CloudLocations {
        return Gson().fromJson(response.body()?.string(), CloudLocations::class.java)
    }
}