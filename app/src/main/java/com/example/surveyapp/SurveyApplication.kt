package com.example.surveyapp

import android.app.Application
import com.example.surveyapp.data.sync.NetworkMonitor
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SurveyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkMonitor.initialize(this)
    }
}