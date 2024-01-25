package com.example.v4uservice

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.previousbooking.*
import kotlinx.android.synthetic.main.updateuserdetail.*
import kotlinx.android.synthetic.main.updateuserdetail.edtName
import kotlinx.android.synthetic.main.updateuserdetail.edtPhone
import kotlinx.android.synthetic.main.userprofile.*
import kotlinx.android.synthetic.main.userprofile.edtAddress


class Updateuserdetails :AppCompatActivity() {
    val user = FirebaseAuth.getInstance().currentUser
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.updateuserdetail)
        btnUpdate.setOnClickListener {
            val Name = edtName.text.toString()
            val Phone = edtPhone.text.toString()
            val Address = edtAaddress.text.toString()
            val userType=tvutype.text.toString()
            val verified=tvverified.text.toString()
            saveFireStore(Name, Phone, Address,userType,verified)

        }

        readFireStoreData()

    }

    fun readFireStoreData() {
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
                val utype: StringBuffer = StringBuffer()
                val verify: StringBuffer = StringBuffer()


                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        name.append(document.data.getValue("Name")).append()
                        phone.append(document.data.getValue("Phone")).append()
                        address.append(document.data.getValue("Address")).append()
                        utype.append(document.data.getValue("userType")).append()
                        verify.append(document.data.getValue("verified")).append()
                    }
                    edtName.setText(name)
                    edtPhone.setText(phone)
                    edtAaddress.setText(address)
                    tvverified.setText(verify)
                    tvutype.setText(utype)
                }
            }
    }

    fun saveFireStore(Name: String, Phone: String, Address: String,userType:String,verified:String) {
        if (edtName.text.toString().isEmpty())
        {
            edtName.error = "Please Enter Name"
            edtName.requestFocus()
            return
        }
        if (edtPhone.text.toString().isEmpty())
        {
            edtPhone.error = "Please Enter Phone Number"
            edtPhone.requestFocus()
            return
        }
        if (edtAaddress.text.toString().isEmpty())
        {
           edtAaddress.error = "Please Enter Address"
           edtAaddress.requestFocus()
            return
        }

        val db = FirebaseFirestore.getInstance()
        val testUser = FirebaseAuth.getInstance().currentUser //getting the current logged in users id
        val userUid = testUser!!.uid
        val uidInput = userUid
        val user: MutableMap<String, Any> = HashMap()
        user["Name"] = Name
        user["Phone"] = Phone
        user["Address"] = Address
        user["userType"]=userType
        user["verified"]=verified
        user["uid"] =  uidInput

        if(user!=null) {
            db.collection("users").document(userUid)
                .set(user)
                .addOnSuccessListener() {
                    Toast.makeText(this, "profile Updated succesfully ", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeScreen::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error while update profile", Toast.LENGTH_SHORT).show()
                }
        }

        }

}


