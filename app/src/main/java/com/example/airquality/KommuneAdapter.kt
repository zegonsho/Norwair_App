package com.example.airquality

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class KommuneAdapter(private val kommuneListe: MutableList<Adapter>): RecyclerView.Adapter<KommuneAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.kommune_name)
        var color: TextView = view.findViewById(R.id.kommune_name)
        var cardView: CardView = view.findViewById(R.id.cardview)
        var weather: TextView = view.findViewById(R.id.weather)
        var weatherValue: TextView = view.findViewById(R.id.weatherValue)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.card, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, pos: Int) {
        viewHolder.name.text = kommuneListe[pos].kommuneNavn
        viewHolder.color.setBackgroundColor(Color.parseColor("#${kommuneListe[pos].fargekode}"))
        viewHolder.weather.text = kommuneListe[pos].vaer.toString()
        viewHolder.weatherValue.text = kommuneListe[pos].beskrivelse.toString()

        viewHolder.cardView.setOnClickListener {
            valgtKommune = kommuneListe[pos]
            val intent = Intent(it.context, StatistikkActivity::class.java)
            it.context.startActivity(intent)
        }
    }
    override fun getItemCount() = kommuneListe.size
}