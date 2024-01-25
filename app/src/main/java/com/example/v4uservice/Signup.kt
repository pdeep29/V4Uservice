package com.example.v4uservice

import android.content.DialogInterface
import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.servicebooking.*
import kotlinx.android.synthetic.main.signup.*
import kotlinx.android.synthetic.main.updateuserdetail.*


class signup : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        auth = FirebaseAuth.getInstance()

        btnlog.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
        btnregister.setOnClickListener {
            val utyp: String = usertype.getSelectedItem().toString()
            signUpUser(utyp)
        }
        var utype = arrayOf("Please Select User Type", "User", "Service Provicer")
        var user = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, utype)
        usertype.adapter = user


        usertype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               // Toast.makeText(this@signup, parent!!.selectedItem.toString(), Toast.LENGTH_LONG).show()
                if (parent!!.selectedItem.toString() == "User") {
                    //Toast.makeText(this@signup, parent!!.selectedItem.toString(), Toast.LENGTH_LONG) .show()

                } else if (parent!!.selectedItem.toString() == "Service Provicer") {
                   // Toast.makeText(this@signup, parent!!.selectedItem.toString(), Toast.LENGTH_LONG).show()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

    }


    private fun signUpUser(utyp: String) {

        if (edtemail.text.toString().isEmpty()) {
            edtemail.error = "Please enter email"
            edtemail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(edtemail.text.toString()).matches()) {
            edtemail.error = "Please enter valid email"
            edtemail.requestFocus()
            return
        }

        if (edtpass.text.toString().isEmpty()) {
            edtpass.error = "Please enter password"
            edtpass.requestFocus()
            return
        }
        if (utyp.equals("User")) {
            auth.createUserWithEmailAndPassword(edtemail.text.toString(), edtpass.text.toString())
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val db = FirebaseFirestore.getInstance()
                                    val testUser =
                                        FirebaseAuth.getInstance().currentUser //getting the current logged in users id
                                    val userUid = testUser!!.uid
                                    val uidInput = userUid
                                    val user: MutableMap<String, Any> = HashMap()
                                    user["uid"] = uidInput
                                    user["userType"] = "user"
                                    user["Name"] = ""
                                    user["Phone"] = ""
                                    user["Address"] = ""
                                    user["verified"] ="verifieduser"

                                    if (user != null) {
                                        db.collection("users").document(userUid)
                                            .set(user)
                                            .addOnSuccessListener() {
                                                //Toast.makeText(this, "profile Updated succesfully ", Toast.LENGTH_SHORT).show()
                                                // startActivity(Intent(this, Userprofile::class.java))
                                                val builder = AlertDialog.Builder(this)
                                                builder.setTitle("Forgot Password")
                                                val view = layoutInflater.inflate(
                                                    R.layout.dialogverifyemailmessege,
                                                    null
                                                )
                                                builder.setView(view)
                                                builder.show()
                                                Handler().postDelayed({
                                                    startActivity(Intent(this, Login::class.java))
                                                    finish()
                                                }, 5000)
                                            }
                                        // .addOnFailureListener {
                                        //   Toast.makeText(this, "Error while update profile", Toast.LENGTH_SHORT).show()
                                        //}
                                    }

                                }
                            }
                    } else {
                        Toast.makeText(
                            baseContext, "Sign Up failed. Try again after some time.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else if (utyp.equals("Service Provicer")) {
            auth.createUserWithEmailAndPassword(edtemail.text.toString(), edtpass.text.toString())
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val db = FirebaseFirestore.getInstance()
                                    val testUser =
                                        FirebaseAuth.getInstance().currentUser //getting the current logged in users id
                                    val userUid = testUser!!.uid
                                    val uidInput = userUid
                                    val user: MutableMap<String, Any> = HashMap()
                                    user["uid"] = uidInput
                                    user["userType"] = "serviceprovider"
                                    user["Name"] = ""
                                    user["Phone"] = ""
                                    user["Address"] = ""


                                    if (user != null) {
                                        db.collection("users").document(userUid)
                                            .set(user)
                                            .addOnSuccessListener() {
                                                startActivity(Intent(this, getspdetail::class.java))
                                                finish()
                                            }
                                         .addOnFailureListener {
                                          Toast.makeText(this, "Error while update profile", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                }
                            }
                    }
                }
        }
        else{
               Toast.makeText(this, "please select from user and service provider", Toast.LENGTH_SHORT).show()
        }
    }
}


