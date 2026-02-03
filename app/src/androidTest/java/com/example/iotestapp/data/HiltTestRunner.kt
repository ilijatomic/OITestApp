package com.example.iotestapp.data

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.example.iotestapp.OIApplication

class HiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        classLoader: ClassLoader,
        className: String,
        context: Context
    ): Application {
        return super.newApplication(classLoader, OIApplication::class.java.name, context)
    }
}