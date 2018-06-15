package com.org.photolocations.data.cloud

import com.org.photolocations.data.model.DBLocation
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock


class CloudManagerTest {

    private var locationsMock = mutableListOf(
            DBLocation(id = "06c417ad-a33f-4c82-8d7b-831b3fab09ee", name = "Milsons Point", latitude = -33.85075, longitude = 151.212519, notes = "", distance = 0.0F),
            DBLocation(id = "d6b9356f-19cc-4526-a4e1-7eb837686dcd", name = "Bondi Beach", latitude = -33.889967, longitude = 151.27644, notes = "", distance = 0.0F),
            DBLocation(id = "f18ac204-cfc8-4878-84f4-6e7bf9dd0042", name = "Circular Quay", latitude = -33.860178, longitude = 151.212706, notes = "", distance = 0.0F),
            DBLocation(id = "87b30b48-b1a1-4792-a928-c80e5ab44627", name = "Manly Beach", latitude = -33.797151, longitude = 151.288784, notes = "", distance = 0.0F),
            DBLocation(id = "d5807308-b2cf-45cb-a9bb-82cec87abb68", name = "Darling Harbour", latitude = -33.873379, longitude = 151.20094, notes = "", distance = 0.0F)
    )

    private val bodyMessage = "testMessage"
    private val defaultLocationLength = 5
    private var dataListener: ICloudRequest? = mock(ICloudRequest::class.java)
    private lateinit var cloudManager: CloudManager

    @Before
    fun init() {
        cloudManager = CloudManager(dataListener!!)
    }

    @Test
    fun mockServer() {

        //check cloud instance
        Assert.assertNotNull(cloudManager)

        //init mock server
        val server = MockWebServer()
        server.enqueue(MockResponse().setBody(bodyMessage))
        server.start()

        //create request
        val bodyOfRequest = OkHttpClient().newCall(cloudManager.createRequest()).execute()
        Assert.assertNotNull(bodyOfRequest)
        Assert.assertNotNull(bodyOfRequest!!.body())
        Assert.assertEquals(true, bodyOfRequest?.isSuccessful)
        Assert.assertEquals(MediaType.parse("application/json"), bodyOfRequest!!.body()!!.contentType())

        //convert response
        val cloudLocations = cloudManager.convertResponse(bodyOfRequest)

        // check updated time
        Assert.assertEquals("2016-12-01T06:52:08Z", cloudLocations.updated)

        //check length array of returned item
        Assert.assertEquals(defaultLocationLength, cloudLocations.locations.size)

        //check arrays of returned item for length
        Assert.assertEquals(locationsMock.size, cloudLocations.locations.size)

        //check each item
        Assert.assertEquals(cloudLocations.locations[0].name, locationsMock[0].name)
        Assert.assertEquals(cloudLocations.locations[1].name, locationsMock[1].name)
        Assert.assertEquals(cloudLocations.locations[2].name, locationsMock[2].name)
        Assert.assertEquals(cloudLocations.locations[3].name, locationsMock[3].name)
        Assert.assertEquals(cloudLocations.locations[4].name, locationsMock[4].name)

        //check any params
        Assert.assertEquals(cloudLocations.locations[0].latitude.toString(), locationsMock[0].latitude.toString())
        Assert.assertEquals(cloudLocations.locations[1].longitude, locationsMock[1].longitude, 0.0)
        Assert.assertEquals(cloudLocations.locations[2].notes, locationsMock[2].notes)
        Assert.assertEquals(cloudLocations.locations[3].distance, locationsMock[3].distance)

    }


}