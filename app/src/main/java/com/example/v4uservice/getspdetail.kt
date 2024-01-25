package com.example.v4uservice

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.spregisterdetails.*
import kotlinx.android.synthetic.main.updateuserdetail.*

class getspdetail:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.spregisterdetails)
        btnreg.setOnClickListener(){
            val Name = editTextTextPersonName.text.toString()
            val Phone =editTextPhone.text.toString()
            val Address = editTextTextPostalAddress.text.toString()
            spregister(Name,Phone,Address)
        }
}
    fun spregister(Name: String, Phone: String, Address: String){
        if (editTextTextPersonName.text.toString().isEmpty())
        {
            editTextTextPersonName.error = "Please Enter Name"
            editTextTextPersonName.requestFocus()
            return
        }
        if (editTextPhone.text.toString().isEmpty())
        {
            editTextPhone.error = "Please Enter Phone Number"
            editTextPhone.requestFocus()
            return
        }
        if (editTextTextPostalAddress.text.toString().isEmpty())
        {
            editTextTextPostalAddress.error = "Please Enter Address"
            editTextTextPostalAddress.requestFocus()
            return
        }
            val db = FirebaseFirestore.getInstance()
            val testUser =
                FirebaseAuth.getInstance().currentUser //getting the current logged in users id
            val userUid = testUser!!.uid
            val uidInput = userUid
            val user: MutableMap<String, Any> = HashMap()
            user["uid"] = uidInput
            user["userType"] = "serviceprovider"
            user["Name"] = Name
            user["Phone"] = Phone
            user["Address"] = Address
          user["verified"] ="notverified"


            if (user != null) {
                db.collection("users").document(userUid)
                    .set(user)
                    .addOnSuccessListener() {

                        startActivity(Intent(this, Login::class.java))

                        finish()
                        Toast.makeText(this, "You will recive E-mail when if you are is succesfull", Toast.LENGTH_SHORT).show()
                    }
                // .addOnFailureListener {
                //   Toast.makeText(this, "Error while update profile", Toast.LENGTH_SHORT).show()
                //}
            }


    }
}