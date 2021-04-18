package com.example.airquality

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class FavorittAdapter(private val favorittList: MutableList<Adapter>): RecyclerView.Adapter<FavorittAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.PLACEHOLDER)
        var color: View = view.findViewById(R.id.color)
        var cardView: CardView = view.findViewById(R.id.cardView)
        var favorittB: CheckBox = view.findViewById(R.id.favorittBoks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavorittAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)
        return FavorittAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favorittList.size
    }

    override fun onBindViewHolder(holder: FavorittAdapter.ViewHolder, position: Int) {
        holder.name.text = favorittList[position].kommuneNavn
        holder.color.setBackgroundColor(Color.parseColor("#${favorittList[position].fargekode}"))
    }

}