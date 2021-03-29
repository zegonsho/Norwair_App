package com.example.airquality

import androidx.recyclerview.widget.RecyclerView

class KommuneAdapter(val kommuner: MutableList<Kommuner>): RecyclerView.Adapter<DataAdapter.ViewHolder>() {
    private val dataList = list
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.PLACEHOLDER)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.card, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, pos: Int) {
        viewHolder.name.text = dataList[pos].PLACEHOLDER
    }
    override fun getItemCount() = dataList.size
}