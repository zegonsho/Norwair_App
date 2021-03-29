package com.example.airquality

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KommuneAdapter(kommuner: MutableList<String>): RecyclerView.Adapter<KommuneAdapter.ViewHolder>() {
    private val dataList = kommuner
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.PLACEHOLDER)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.card, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, pos: Int) {
        viewHolder.name.text = dataList[pos]
    }
    override fun getItemCount() = dataList.size
}