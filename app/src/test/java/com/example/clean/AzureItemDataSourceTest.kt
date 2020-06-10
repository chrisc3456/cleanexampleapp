package com.example.clean

import com.example.clean.api.AzureFunctionService
import com.example.clean.api.AzureResponseItem
import com.example.clean.data.Result
import com.example.clean.data.sources.AzureItemDataSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.*

class AzureItemDataSourceTest {

    @Test
    fun getItems_serviceProvided_serviceGetItemsCalled() {
        val service = mock(AzureFunctionService::class.java)
        val dataSource = AzureItemDataSource(service)
        dataSource.getItems()
        verify(service).getItems()
    }

    @Test
    fun getItems_successfulServiceCallNoItems_successResultEmptyList() {
        val service = MockSuccessAzureFunctionService(null)
        val dataSource = AzureItemDataSource(service)

        val result = dataSource.getItems()

        assertTrue(result is Result.Success)
        assertTrue((result as Result.Success).data.isEmpty())
    }

    @Test
    fun getItems_successfulServiceCallItems_successResultWithItems() {
        val service = MockSuccessAzureFunctionService(listOf(
            AzureResponseItem("", "", "", "", "")
        ))
        val dataSource = AzureItemDataSource(service)

        val result = dataSource.getItems()

        assertTrue(result is Result.Success)
        assertEquals(1, (result as Result.Success).data.size)

    }

    // GRADLE DEPENDENCY ISSUES WHEN USING POWERMOCKITO FOR THESE TESTS
    /*@Test
    fun getItems_unsuccessfulServiceCall_errorResultWithException() {
        val service = MockErrorAzureFunctionService()
        val dataSource = AzureItemDataSource(service)

        // PowerMockito allows for mocking of static classes, e.g. in this case where there's a dependency on Resources.getSystem() for retrieving string resources
        PowerMockito.mockStatic(Resources::class.java, RETURNS_MOCKS)
        val res = mock(Resources.getSystem().javaClass)
        PowerMockito.`when`(Resources.getSystem()).thenReturn(res)
        `when`(res.getString(anyInt())).thenReturn("test")

        val result = dataSource.getItems()

        assertTrue(result is Result.Error)
        assertEquals("test", (result as Result.Error).exception.message)
    }

    @Test
    fun getItems_serviceCallException_errorResultWithException() {
        val res = mock(Resources.getSystem().javaClass)
        `when`(Resources.getSystem()).thenReturn(res)
        `when`(res.getString(anyInt())).thenReturn("message")
    }*/
}