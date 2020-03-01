package com.cactus

import android.app.Application
import com.google.firebase.FirebaseApp

class CactusApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
