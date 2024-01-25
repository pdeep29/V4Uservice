package com.example.v4uservice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.admin_spdetails.view.*

class spdetailadapter(options: FirestoreRecyclerOptions<Spdetails>):FirestoreRecyclerAdapter<Spdetails,spdetailadapter.spdetailadaptervh>(options) {
    var ctx: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): spdetailadaptervh {
        ctx = parent.context

        return spdetailadapter.spdetailadaptervh(
            LayoutInflater.from(parent.context).inflate(
                R.layout.admin_spdetails,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: spdetailadaptervh, position: Int, model: Spdetails) {
        holder.nm.text = model.Name;
        holder.ph.text = model.Phone;
        holder.add.text = model.Address;
        holder.stat.text = model.verified;
    }


    class spdetailadaptervh(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nm = itemView.tvnam
        var ph = itemView.tvphone
        var add = itemView.tvaddress
        var stat = itemView.tvvstat
    }
}
