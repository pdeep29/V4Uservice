package com.example.v4uservice

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.userprofile.*


class Userprofile :AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private lateinit var auth: FirebaseAuth

    private var tvEmail: TextView? = null
    private var tvEmailVerifiied: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.userprofile)
        auth = FirebaseAuth.getInstance()
        btnlogout.setOnClickListener {
            auth.signOut()
            Intent(this, Login::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
        btnchng.setOnClickListener {
            startActivity(Intent(this, Changepassword::class.java))
        }
        btnupdate.setOnClickListener {

                startActivity(Intent(this, Updateuserdetails::class.java))


        }

        initialise()
        readFireStoreData()
    }

    override fun onStart() {
        super.onStart()
        val mUser = auth!!.currentUser
        if (mUser != null) {
            tvEmail!!.text = mUser.email.toString()
        }
        if (mUser != null) {
            tvEmailVerifiied!!.text = mUser.isEmailVerified.toString()
        }
    }

    private fun initialise() {

        mAuth = FirebaseAuth.getInstance()

        tvEmail = findViewById<View>(R.id.tv_email) as TextView
        tvEmailVerifiied = findViewById<View>(R.id.tv_email_verifiied) as TextView
    }

    private fun readFireStoreData() {

        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val current = user!!.uid
        db.collection("users")//.document(current)
            .whereEqualTo("uid",current)
            .get()
            .addOnCompleteListener {

                val name: StringBuffer = StringBuffer()
                val phone: StringBuffer = StringBuffer()
                val address: StringBuffer = StringBuffer()

                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        name.append(document.data.getValue("Name")).append()
                           phone.append(document.data.getValue("Phone")).append()
                            address.append(document.data.getValue("Address")).append()
                    }
                    edtName.setText(name)
                    edtphone.setText(phone)
                    edtAddress.setText(address)
                }
            }
    }
}