package com.example.v4uservice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.book_detail.*
import kotlinx.android.synthetic.main.previousbooking.*
import kotlinx.android.synthetic.main.previousbooking.view.*
import kotlinx.android.synthetic.main.splash_activity.*
import kotlinx.android.synthetic.main.userprofile.*
import java.text.SimpleDateFormat
import java.util.*


class PreviousBookings :AppCompatActivity() {

private val db:FirebaseFirestore= FirebaseFirestore.getInstance();
    //private val collectionReference:CollectionReference=db.collection("Bookings").whereEqualTo("uid",current);
    var Bdetailadpater: bdetailadpater? =null;

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.previousbooking)
        setUpRecyclerview()
        readFireStoreData()
    }

    fun setUpRecyclerview(){

        val user = FirebaseAuth.getInstance().currentUser
        val current = user!!.uid
        val query:Query=  db.collection("Bookings")
            .whereEqualTo("uid",current)
        val  firestoreRecyclerOptions:FirestoreRecyclerOptions<bdetail> = FirestoreRecyclerOptions.Builder<bdetail>()
            .setQuery(query,bdetail::class.java)
            .build();
        Bdetailadpater = bdetailadpater(firestoreRecyclerOptions);
        recyclerview.layoutManager=LinearLayoutManager(this)
        recyclerview.adapter =Bdetailadpater


    }

    override fun onStart() {
        super.onStart()
        Bdetailadpater!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        Bdetailadpater!!.stopListening()
    }

    fun readFireStoreData() {
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val current = user!!.uid
        db.collection("users")
            .whereEqualTo("uid", current)
            .get()
            .addOnCompleteListener {

                val uid: StringBuffer = StringBuffer()

                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        uid.append(document.data.getValue("uid")).append()

                    }
                    tvuid.setText(uid)


                }

            }
    }


}