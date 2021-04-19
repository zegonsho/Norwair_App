package com.example.airquality

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class KommuneAdapter(private val adapterList: MutableList<Adapter>): RecyclerView.Adapter<KommuneAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.PLACEHOLDER)
        var color: View = view.findViewById(R.id.color)
        var cardView: CardView = view.findViewById(R.id.cardView)
        var weather: TextView = view.findViewById(R.id.weather)
        var weatherValue: TextView = view.findViewById(R.id.weatherValue)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.card, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, pos: Int) {

        viewHolder.name.text = com.example.airquality.adapterList[pos].kommuneNavn
        viewHolder.color.setBackgroundColor(Color.parseColor("#${com.example.airquality.adapterList[pos].fargekode}"))
        viewHolder.weather.text = com.example.airquality.adapterList[pos].vaer.toString()
        viewHolder.weatherValue.text = com.example.airquality.adapterList[pos].beskrivelse.toString()

        viewHolder.cardView.setOnClickListener {
            valgtKommune = com.example.airquality.adapterList[pos]
            val intent = Intent(it.context, StatisticsActivity::class.java)
            it.context.startActivity(intent)
        }
    }
    override fun getItemCount() = adapterList.size
}