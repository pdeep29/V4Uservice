package com.example.v4uservice

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.book_detail.view.*
import kotlinx.android.synthetic.main.servicebooking.*
import java.text.SimpleDateFormat
import java.util.*


class bdetailadpater(options: FirestoreRecyclerOptions<bdetail>) :
    FirestoreRecyclerAdapter<bdetail, bdetailadpater.bdetailadaptervh>(options)  {
    var ctx: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bdetailadaptervh {
        ctx=parent.context

        return bdetailadaptervh(
            LayoutInflater.from(parent.context).inflate(
                R.layout.book_detail,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: bdetailadaptervh, position: Int, model: bdetail) {
        holder.nm.text= model.Name;
        holder.ph.text=model.Phone;
        holder.add.text=model.Address;
        holder.dt.text=model.Date;
        holder.sc.text=model.ServiceCenter;
        holder.st.text=model.ServiceType;
        holder.stm.text=model.SlotTime;
        holder.vno.text=model.Vehicleno;
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        holder.edtbtn.setOnClickListener{
            val intent:Intent = Intent(it.context, UpdateBooking::class.java)
            intent.putExtra("EXTRA_SESSION_ID", model.uid)
            intent.putExtra("EXTRA_SESSION_DATE", model.Date)
            ctx!!.startActivity(intent)


            Toast.makeText(it.context, "Clicked on edit menu", Toast.LENGTH_LONG).show()
        }
        holder.delbtn.setOnClickListener {
            Toast.makeText(it.context, "Clicked on delete menu", Toast.LENGTH_LONG).show()
            val db = FirebaseFirestore.getInstance()

            db.collection("Bookings").whereEqualTo("uid", model.uid).whereEqualTo("Date",model.Date).get()
                .addOnCompleteListener {


                    if (it.isSuccessful) {
                        var document = it.result!!
                        var uid: String = ""
                        for (document in it.result!!) {
                            uid = document.id
                            Toast.makeText(ctx, uid, Toast.LENGTH_LONG).show()
                        }
                        db.collection("Bookings").document(uid).delete().addOnCompleteListener {
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
    class bdetailadaptervh(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nm= itemView.tvnm
        var ph= itemView.tvph
        var add= itemView.tvadd
        var dt= itemView.tvdt
        var sc= itemView.tvss
        var st= itemView.tvstp
        var stm= itemView.tvstm
        var vno= itemView.tvvno
        var delbtn=itemView.ibtndelete
        var edtbtn=itemView.ibtnupdate
        var tdt=itemView.tvdtt
    }




}