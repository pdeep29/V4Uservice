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
import kotlinx.android.synthetic.main.sprequest.*

class adminsprequest:AppCompatActivity() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance();
    //private val collectionReference:CollectionReference=db.collection("Bookings").whereEqualTo("uid",current);
    var Sprequestadapt: sprequesadapter? =null;

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sprequest)
        setUpRecyclerview()
      //  readFireStoreData()
    }

    fun setUpRecyclerview(){


        val query: Query =  db.collection("users")
           .whereEqualTo("userType","serviceprovider")
          .whereEqualTo("verified","notverified")
        val  firestoreRecyclerOptions: FirestoreRecyclerOptions<Spdetails> = FirestoreRecyclerOptions.Builder<Spdetails>()
            .setQuery(query,Spdetails::class.java)
            .build();
        Sprequestadapt = sprequesadapter(firestoreRecyclerOptions);
        rvsprequest.layoutManager= LinearLayoutManager(this)
        rvsprequest.adapter =Sprequestadapt


    }

    override fun onStart() {
        super.onStart()
        Sprequestadapt!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        Sprequestadapt!!.stopListening()
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