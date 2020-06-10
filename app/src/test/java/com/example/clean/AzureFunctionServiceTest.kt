package com.example.clean

import com.example.clean.api.AzureFunctionService
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AzureFunctionServiceTest {

    private lateinit var server: MockWebServer
    private lateinit var retrofit: Retrofit

    @Before
    fun setup() {
        server = MockWebServer()
        retrofit = Retrofit.Builder()
            .baseUrl(server.url("").toString())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @After
    fun shutdown() {
        server.shutdown()
    }

    @Test
    fun getItems_noItemsInFile_noItemsReturned() {
        val response = MockResponse().setBody(MockResponseFileReader("azurefunctionservice_getItems_noItems.json").content)
        server.enqueue(response)

        val result = retrofit.create(AzureFunctionService::class.java)
            .getItems()
            .execute()

        assertTrue(result.body()!!.isEmpty())
    }

    @Test
    fun getItems_twoItemsInFile_twoItemsReturned() {
        val response = MockResponse().setBody(MockResponseFileReader("azurefunctionservice_getItems_twoItems.json").content)
        server.enqueue(response)

        val result = retrofit.create(AzureFunctionService::class.java)
            .getItems()
            .execute()

        assertEquals(2, result.body()!!.size)
    }

    @Test
    fun getItems_oneItemInFile_oneResultPopulatedCorrectly() {
        val response = MockResponse().setBody(MockResponseFileReader("azurefunctionservice_getItems_oneItem.json").content)
        server.enqueue(response)

        val result = retrofit.create(AzureFunctionService::class.java)
            .getItems()
            .execute()
            .body()!!
            .first()

        assertEquals("TestId", result.id)
        assertEquals("TestDate", result.date)
        assertEquals("TestTitle", result.title)
        assertEquals("TestExcerpt", result.excerpt)
        assertEquals("TestContent", result.content)
    }
}