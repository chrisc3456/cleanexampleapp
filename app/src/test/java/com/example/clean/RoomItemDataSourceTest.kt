package com.example.clean

import com.example.clean.api.AzureResponseItem
import com.example.clean.data.localdb.ItemDatabase
import com.example.clean.data.localdb.ItemEntity
import com.example.clean.data.localdb.ItemEntityDao
import com.example.clean.data.sources.RoomItemDataSource
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.*

class RoomItemDataSourceTest {

    private lateinit var dataSource: RoomItemDataSource
    @Mock private lateinit var database: ItemDatabase
    @Mock private lateinit var dao: ItemEntityDao

    @Before
    fun setup() {
        database = mock(ItemDatabase::class.java)
        dao = mock(ItemEntityDao::class.java)
        dataSource = RoomItemDataSource(database)
        `when`(database.itemDao()).thenReturn(dao)
    }

    @Test
    fun getItems_databaseProvided_daoCalled() {
        dataSource.getItems()
        verify(database.itemDao()).getItems()
    }

    @Test
    fun getItems_daoHasItems_itemsReturned() {
        `when`(dao.getItems()).thenReturn(
            listOf(ItemEntity("", "", "", "", ""))
        )

        val items = dataSource.getItems()
        assertEquals(1, items.size)
    }

    @Test
    fun getItem_databaseAndIdProvided_daoCalled() {
        dataSource.getItem("test")
        verify(database.itemDao()).getItem("test")
    }

    @Test
    fun getItem_daoHasItem_itemReturned() {
        val item = ItemEntity("", "", "", "", "")
        `when`(dao.getItem("")).thenReturn(item)

        assertEquals(item, dataSource.getItem(""))
    }

    @Test
    fun updateWithRemoteItems_itemsProvided_addItemsCalled() {
        dataSource.updateWithRemoteItems(listOf())
        verify(database.itemDao()).addItems(listOf())
    }

    @Test
    fun updateWithRemoteItems_itemsProvidedWithValue_itemsAddedWithMatchingValue() {
        val item = AzureResponseItem("A", "B", "C", "D", "E")
        dataSource.updateWithRemoteItems(listOf(item))
        verify(database.itemDao()).addItems(listOf(ItemEntity("A", "B", "C", "D", "E")))
    }
}