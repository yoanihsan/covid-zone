package com.idn.covid19

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Splash  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // langsung pindah ke MainActivity atau activity lain
        // begitu memasuki splash screen ini
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}