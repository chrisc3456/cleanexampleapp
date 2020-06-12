package com.example.clean

import org.junit.Test
import org.junit.Assert.*

class MockResponseFileReaderTest {

    @Test
    fun content_fileProvided_contentPopulated() {
        val reader = MockResponseFileReader("mockResponseFileReader_test")
        assertEquals("success", reader.content)
    }
}