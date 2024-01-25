package com.example.v4uservice

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.admin_spdetails.view.*
import kotlinx.android.synthetic.main.book_detail.view.*
import kotlinx.android.synthetic.main.spbookinga.view.*
import kotlinx.android.synthetic.main.spbookinga.view.tvadd
import kotlinx.android.synthetic.main.spbookinga.view.tvdt
import kotlinx.android.synthetic.main.spbookinga.view.tvnm
import kotlinx.android.synthetic.main.spbookinga.view.tvph
import kotlinx.android.synthetic.main.spbookinga.view.tvss
import kotlinx.android.synthetic.main.spbookinga.view.tvstm
import kotlinx.android.synthetic.main.spbookinga.view.tvstp
import kotlinx.android.synthetic.main.spbookinga.view.tvvno

class spbookingadapter(options: FirestoreRecyclerOptions<bdetail>):FirestoreRecyclerAdapter<bdetail,spbookingadapter.spbookingadaptervh>(options) {

    var ctx: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): spbookingadaptervh {
        ctx = parent.context

        return spbookingadapter.spbookingadaptervh(
            LayoutInflater.from(parent.context).inflate(
                R.layout.spbookinga,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: spbookingadaptervh, position: Int, model: bdetail) {
        holder.nm.text= model.Name;
        holder.ph.text=model.Phone;
        holder.add.text=model.Address;
        holder.dt.text=model.Date;
        holder.sc.text=model.ServiceCenter;
        holder.st.text=model.ServiceType;
        holder.stm.text=model.SlotTime;
        holder.vno.text=model.Vehicleno;
    }
    class spbookingadaptervh(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nm= itemView.tvnm
        var ph= itemView.tvph
        var add= itemView.tvadd
        var dt= itemView.tvdt
        var sc= itemView.tvss
        var st= itemView.tvstp
        var stm= itemView.tvstm
        var vno= itemView.tvvno

    }

}