package com.org.photolocations.data.cloud

import com.org.photolocations.data.model.CloudLocations

interface ICloudRequest {
    fun success(cloudLocations: CloudLocations)
    fun error(errorMessage: String?)
}