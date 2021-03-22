package com.example.airquality

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

data class DataAdapter(val list: MutableList<Data>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {
    private val dataList = list
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Insert values in cardview
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.card, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, pos: Int) {
        // Insert changes to values in cardview
    }
    override fun getItemCount() = dataList.size
}