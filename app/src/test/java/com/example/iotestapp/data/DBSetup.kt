package com.example.iotestapp.data

import androidx.room.Room
import com.example.iotestapp.data.local.AppDatabase
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
open class DBSetup {
    lateinit var db: AppDatabase

    @Before
    open fun setUp() {
        val context = RuntimeEnvironment.getApplication()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    open fun tearDown() {
        db.close()
    }
}