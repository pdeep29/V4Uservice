package com.example.v4uservice

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.changepass.*

class Changepassword : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.changepass)
        auth = FirebaseAuth.getInstance()
        btnchangepassword.setOnClickListener {
            changePassword()
        }
        btncancel.setOnClickListener{
            goHome()
        }
    }
    private fun goHome(){
        startActivity(Intent(this, HomeScreen::class.java))
    }
    private fun changePassword() {

        if (edtcrpass.text.isNotEmpty() &&
            edtnewpass.text.isNotEmpty() &&
            edtreenterpass.text.isNotEmpty()
        ) {

            if (edtnewpass.text.toString().equals(edtreenterpass.text.toString())) {

                val user = auth.currentUser
                if (user != null && user.email != null) {
                    val credential = EmailAuthProvider
                        .getCredential(user.email!!, edtcrpass.text.toString())

// Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this, "Re-Authentication success.", Toast.LENGTH_SHORT).show()
                                user.updatePassword(edtnewpass.text.toString())
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(this, "Password changed successfully.", Toast.LENGTH_SHORT).show()
                                            auth.signOut()
                                            startActivity(Intent(this, Login::class.java))
                                            finish()
                                        }
                                    }

                            } else {
                                Toast.makeText(this, "Re-Authentication failed.", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }

            } else {
                Toast.makeText(this, "Password mismatching.", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(this, "Please enter all the fields.", Toast.LENGTH_SHORT).show()
        }

    }
}