package com.example.clockCustomView

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber

class ClockApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}