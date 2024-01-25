package com.example.v4uservice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.homescreen.*

class HomeScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homescreen)

        auth = FirebaseAuth.getInstance()


        card1.setOnClickListener {
           // Toast.makeText(this, "Clicked on Profile menu", Toast.LENGTH_LONG).show()
            var i: Intent = Intent(this, Userprofile::class.java)
            startActivity(i)
        }
        card2.setOnClickListener {
           // Toast.makeText(this, "Clicked on Service", Toast.LENGTH_LONG).show()
            var j: Intent = Intent(this, BookService::class.java)
            startActivity(j)
        }
        card3.setOnClickListener {
           // Toast.makeText(this, "Clicked on Booking menu", Toast.LENGTH_LONG).show()
            var k: Intent = Intent(this, PreviousBookings::class.java)
            startActivity(k)
        }
        card4.setOnClickListener {
            auth.signOut()
            Intent(this, Login::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
                val prefs = getSharedPreferences("sharedpref", Context.MODE_PRIVATE)
                prefs.edit(commit = true) {
                    clear()
                }

            }

        }
    }
}


