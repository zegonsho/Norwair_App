package com.example.airquality

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView


class KommuneAdapter(private val kommuneListe: MutableList<Adapter>, context: Context): RecyclerView.Adapter<KommuneAdapter.ViewHolder>(){
    private val context: Context? = context
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.kommune_name)
        var color: TextView = view.findViewById(R.id.kommune_name)
        var cardView: CardView = view.findViewById(R.id.cardview)
        var weather: TextView = view.findViewById(R.id.weather)
        //var weather: ImageView = view.findViewById(R.id.weather)
        var aqi: TextView = view.findViewById(R.id.AQIValue)
        var weatherValue: TextView = view.findViewById(R.id.weatherValue)
        var favorittB: CheckBox = view.findViewById(R.id.favorittBoks)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.card, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, pos: Int) {
        viewHolder.name.text = kommuneListe[pos].kommuneNavn
        viewHolder.color.setBackgroundColor(Color.parseColor("#${kommuneListe[pos].fargekode}"))
        //viewHolder.weather.text = kommuneListe[pos].beskrivelse.toString()
        kommuneListe[pos].ikonID?.let { viewHolder.weather.setBackgroundResource(it) }
        viewHolder.weatherValue.text = kommuneListe[pos].vaer.toString()

        viewHolder.aqi.text = kommuneListe[pos].aqiVal?.toInt().toString()

        viewHolder.cardView.setOnClickListener {
            valgtKommune = kommuneListe[pos]
            (context as AppCompatActivity).supportActionBar!!.title = "Statistics ${valgtKommune.kommuneNavn}"
            val transaction = context.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, StatsFragment.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        }
        viewHolder.favorittB.setOnClickListener {
            if (viewHolder.favorittB.isChecked) {
                favorittList.add(kommuneListe[pos])
                Log.d("list size after adding:", favorittList.size.toString())
                Log.d("Added", kommuneListe[pos].kommuneNavn.toString())
            } else {
                favorittList.remove(kommuneListe[pos])
                Log.d("list size after rm: ", favorittList.size.toString())
                Log.d("Removed", kommuneListe[pos].kommuneNavn.toString())
            }
        }
    }
    override fun getItemCount() = kommuneListe.size
}