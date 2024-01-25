package com.example.v4uservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_spbooking.*
import kotlinx.android.synthetic.main.book_detail.*
import kotlinx.android.synthetic.main.previousbooking.*
import kotlinx.android.synthetic.main.userprofile.*

class spbooking : AppCompatActivity() {
    private val db:FirebaseFirestore= FirebaseFirestore.getInstance();
    var Spbookingadapter: spbookingadapter? =null;

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spbooking)
            //  readFireStoreData()
        setUpRecyclerview()

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
                        uid.append(document.data.getValue("Address")).append()
                        //Toast.makeText(this,uid,Toast.LENGTH_LONG).show()
                    }


                }
                tvaddress.setText(uid)
            }



    }
    fun setUpRecyclerview(){
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val current = user!!.uid
        val query: Query =  db.collection("Bookings")
            .whereEqualTo("spid",current)
        val  firestoreRecyclerOptions: FirestoreRecyclerOptions<bdetail> = FirestoreRecyclerOptions.Builder<bdetail>()
            .setQuery(query,bdetail::class.java)
            .build();
        Spbookingadapter = spbookingadapter(firestoreRecyclerOptions);
        rvspbooking.layoutManager=LinearLayoutManager(this)
        rvspbooking.adapter =Spbookingadapter


    }

    override fun onStart() {
        super.onStart()
        Spbookingadapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        Spbookingadapter!!.stopListening()
    }




}



