package com.example.v4uservice


import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.login.*


class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        auth = FirebaseAuth.getInstance()

        btnregister1.setOnClickListener {
            startActivity(Intent(this, signup::class.java))
            finish()
        }

        btnlog1.setOnClickListener {
            dologin()
            progressBar.visibility=View.VISIBLE
         //   val builder = AlertDialog.Builder(this)
          //  builder.setTitle("Loading....")
          //  val view = layoutInflater.inflate(R.layout.loading, null)
          //  val username = view.findViewById<ProgressBar>(R.id.progressBar2)
          //  builder.setView(view)
          //  builder.show()
        }

        btnforgotpassword.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Forgot Password")
            val view = layoutInflater.inflate(R.layout.dialogforgotpassword, null)
            val username = view.findViewById<EditText>(R.id.edtdialogforgot)
            builder.setView(view)
            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { _, _ ->
                forgotPassword(username)
            })
            builder.setNegativeButton("close", DialogInterface.OnClickListener { _, _ -> })
            builder.show()
        }

    }

    private fun forgotPassword(username: EditText) {
        if (username.text.toString().isEmpty()) {
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            return
        }

        auth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Email sent.", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun dologin() {
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

        auth.signInWithEmailAndPassword(edtemail.text.toString(), edtpass.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val db = FirebaseFirestore.getInstance()
                    val current = user!!.uid
                    db.collection("users")//.document(current)
                        .whereEqualTo("uid", current)
                        .get()
                        .addOnCompleteListener {
                            val verify: StringBuffer = StringBuffer()
                            val stat: StringBuffer = StringBuffer()
                            if (it.isSuccessful) {
                                for (document in it.result!!) {
                                    stat.append(document.data.getValue("verified")).append()
                                    verify.append(document.data.getValue("userType")).append()
                                }
                                edtverify.setText(verify)
                                tvstatus.setText(stat)
                                Toast.makeText(this, edtverify.text.toString(), Toast.LENGTH_LONG).show()
                                val utype=edtverify.text.toString()

                                val sharedPreferences =getSharedPreferences("sharedpref", Context.MODE_PRIVATE)
                                val editor=sharedPreferences.edit()
                                editor.apply(){
                                    putString("STRING_KEY", utype)

                                }.apply()
                            }
                        }

                    updateUI(user)

                } else {
                    updateUI(null)
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        val db = FirebaseFirestore.getInstance()
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {

        if (currentUser != null ) {
            if (currentUser.isEmailVerified) {
               println("edtverify.text ${edtverify.text.toString()}")
                if (edtverify.text.equals("user")){
                    startActivity(Intent(this, HomeScreen::class.java))
                    finish()
                }
                else if (edtverify.text.equals("serviceprovider")){
                    if (tvstatus.text.equals("verified")){
                        startActivity(Intent(this, sphome::class.java))
                        finish()
                    }
                    else
                    {
                        Toast.makeText(this, "your email is in verification stage please contact admin", Toast.LENGTH_SHORT).show()
                    }
                }
                else if (edtverify.text==("Admin")) {
                        startActivity(Intent(this, AdminHome::class.java))
                        finish()
                    }
            }
            else {
                Toast.makeText(baseContext, "Please verify your email address and password.", Toast.LENGTH_SHORT).show()
            }
        } else {
            //Toast.makeText(baseContext, "Login failed.", Toast.LENGTH_SHORT).show()
        }
    }

}

