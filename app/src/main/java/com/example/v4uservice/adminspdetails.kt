package com.example.v4uservice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.previousbooking.*
import kotlinx.android.synthetic.main.previousbooking.tvuid
import kotlinx.android.synthetic.main.spdisplay.*

class adminspdetails:AppCompatActivity() {
    private val db:FirebaseFirestore= FirebaseFirestore.getInstance();
    var SPdetailadpater: spdetailadapter? =null;

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.spdisplay)
        setUpRecyclerview()

    }

    fun setUpRecyclerview(){

        val query: Query =  db.collection("users")
            .whereEqualTo("userType","serviceprovider")
            .whereEqualTo("verified","verified")
        val  firestoreRecyclerOptions: FirestoreRecyclerOptions<Spdetails> = FirestoreRecyclerOptions.Builder<Spdetails>()
            .setQuery(query,Spdetails::class.java)
            .build();
        SPdetailadpater = spdetailadapter(firestoreRecyclerOptions);
        rvspadd.layoutManager= LinearLayoutManager(this)
        rvspadd.adapter =SPdetailadpater


    }

    override fun onStart() {
        super.onStart()
        SPdetailadpater!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        SPdetailadpater!!.stopListening()
    }



}