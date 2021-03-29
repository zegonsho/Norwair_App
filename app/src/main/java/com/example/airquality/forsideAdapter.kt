package com.example.chakkh_oblig2

import android.annotation.SuppressLint
import android.graphics.Color
import android.provider.Telephony
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import java.math.RoundingMode
import java.text.DecimalFormat


class PartyAdapter(private val kommuner: MutableList<Kommune>) :
        RecyclerView.Adapter<PartyAdapter.ViewHolder>() {

    class ViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView) {

    }

    override fun getItemCount(): Int {
        return kommuner.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyAdapter.ViewHolder {
        val view1 = LayoutInflater.from(parent.context).inflate(R.layout.element, parent, false)
        return ViewHolder()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewHolder = kommuner[position]
    }


}

