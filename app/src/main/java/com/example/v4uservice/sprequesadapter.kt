package com.example.v4uservice

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.admin_spdetails.view.*
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.admin_spdetailrequest.view.*
import kotlinx.android.synthetic.main.admin_spdetails.view.tvaddress
import kotlinx.android.synthetic.main.admin_spdetails.view.tvnam
import kotlinx.android.synthetic.main.admin_spdetails.view.tvphone
import kotlinx.android.synthetic.main.admin_spdetails.view.tvvstat
import kotlinx.android.synthetic.main.previousbooking.*

class sprequesadapter(options: FirestoreRecyclerOptions<Spdetails>):FirestoreRecyclerAdapter<Spdetails,sprequesadapter.sprequestadaptervh>(options)

{
    private lateinit var auth: FirebaseAuth
    var ctx: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sprequesadapter.sprequestadaptervh {
        ctx = parent.context

        return sprequesadapter.sprequestadaptervh(
            LayoutInflater.from(parent.context).inflate(
                R.layout.admin_spdetailrequest,
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: sprequesadapter.sprequestadaptervh, position: Int, model: Spdetails) {
        holder.nm.text = model.Name;
        holder.ph.text = model.Phone;
        holder.add.text = model.Address;
        holder.stat.text = model.verified;
        holder.uid.text=model.uid
        holder.btnaccept.setOnClickListener{

            val db = FirebaseFirestore.getInstance()

            val userUid = model.uid


            val user: MutableMap<String, Any> = HashMap()
            user["Name"] = holder.nm.text.toString()
            user["Phone"] = holder.ph.text.toString()
            user["Address"] =holder.add.text.toString()
            user["verified"] ="verified"
            user["uid"]= holder.uid.text.toString()
            user["userType"]="serviceprovider"


            if(user!=null) {
                db.collection("users").document(userUid!!)
                    .set(user)

                    .addOnSuccessListener() {
                        Toast.makeText(ctx, "profile Updated succesfully ", Toast.LENGTH_SHORT).show()
                        holder.stat.text="verified"

                    }

                    .addOnFailureListener {
                        Toast.makeText(ctx, "Error while update profile", Toast.LENGTH_SHORT).show()
                    }
            }


            }
        holder.btndelet.setOnClickListener{
            val db = FirebaseFirestore.getInstance()

            db.collection("users").whereEqualTo("uid", model.uid).get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        var document = it.result!!
                        var uid: String = ""
                        for (document in it.result!!) {
                            uid = document.id
                            Toast.makeText(ctx, uid, Toast.LENGTH_LONG).show()
                        }
                        db.collection("users").document(uid).delete().addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(ctx, "Clicked on delete menu", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(ctx, "Clicked on delete menu", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }

        }


        

    }

    class sprequestadaptervh (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var uid =itemView.tvuid
        var nm = itemView.tvnam
        var ph = itemView.tvphone
        var add = itemView.tvaddress
        var stat = itemView.tvvstat
        var btnaccept=itemView.ivaccept
        var btndelet=itemView.ivdelete
    }

}