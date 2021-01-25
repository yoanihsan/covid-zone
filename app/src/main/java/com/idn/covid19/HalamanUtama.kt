package com.idn.covid19

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HalamanUtama  : AppCompatActivity() {
    private var txtUsername: TextView? = null
    private var keluar: Button? = null
    private var info: Button? = null
    private var infoProv: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_utama)
        txtUsername = findViewById<View>(R.id.txthasiluser) as TextView
        val i = intent
        val txthasiluser = i.getStringExtra("username")
        val txthasilpass = i.getStringExtra("password")
        txtUsername!!.text = txthasiluser.toString()
        keluar = findViewById<View>(R.id.btnExit) as Button
        keluar!!.setOnClickListener {
            exit()
        }
        info = findViewById<View>(R.id.btnInfoCovid) as Button
        info!!.setOnClickListener{
            val i = Intent(this@HalamanUtama, MainActivity::class.java)
            startActivity(i)
        }

        infoProv = findViewById<View>(R.id.btnInfoCovidProvinsi) as Button
        infoProv!!.setOnClickListener{
            val i = Intent(this@HalamanUtama, MainActivityProv::class.java)
            startActivity(i)
        }
    }

    fun onClick(clicked: View) {
        when (clicked.id) {
            R.id.btnExit -> exit()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //jika tombol BACK ditekan
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exit() {
        val builder = AlertDialog.Builder(this)
            .setMessage("Apakah Kamu Benar-Benar ingin keluar?")
            .setNegativeButton(
                "Tidak"
            ) { dialog, which -> dialog.dismiss() }
            .setPositiveButton("Ya") { dialog, which ->
                dialog.dismiss()
                finish()
            }
        val dialog = builder.create()
        dialog.show()
    }

}

