package com.example.v4uservice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.splash_activity.*

class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        val backrground= object :Thread(){
            override fun run() {
                try {
                    Thread.sleep(2500)
                  //  val intent =Intent(baseContext,Login::class.java)
                    //startActivity(intent)
                        loaddata()
                }
                catch (e:Exception){

                    e.printStackTrace()
                }

            }
        }
        backrground.start()
    }
    private fun loaddata(){
        val sharedPreferences= getSharedPreferences("sharedpref",Context.MODE_PRIVATE)
        val savedString=sharedPreferences.getString("STRING_KEY",null)
        logverify.text =savedString
        if (logverify.text.equals("user")){
            startActivity(Intent(this, HomeScreen::class.java))
            finish()
        }
         else if (logverify.text.equals("serviceprovider")){
            startActivity(Intent(this, sphome::class.java))
            finish()
        }
        else if (logverify.text.equals("Admin")){
            startActivity(Intent(this, AdminHome::class.java))
            finish()
        }
        else{
            startActivity(Intent(this, Login::class.java))
            finish()

        }
    }
}
