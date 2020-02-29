package com.cactus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    enum class Arguments { URI }
}
