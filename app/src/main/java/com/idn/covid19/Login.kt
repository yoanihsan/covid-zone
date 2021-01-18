package com.idn.covid19

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {
    private var btnlng: Button? = null
    private var inuser: EditText? = null
    private var inpass: EditText? = null

    //public String username, password;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        inuser = findViewById<View>(R.id.inuser) as EditText
        inpass = findViewById<View>(R.id.inpass) as EditText
        btnlng = findViewById<View>(R.id.btnlng) as Button
        btnlng!!.setOnClickListener {
            if (inuser!!.text.toString() == "yoanihsan" &&  inpass!!.text.toString() == "123456"
            ) {
                Toast.makeText(
                    applicationContext,
                    "Anda Login Sebagai : " + inuser!!.text.toString() + " dan Password : " + inpass!!.text.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                val i = Intent(this@Login, HalamanUtama::class.java)
                i.putExtra("username", inuser!!.text.toString())
                i.putExtra("password", inpass!!.text.toString())
                startActivity(i)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Username dan Pssword tidak sesuai Anda gagal Login",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}