package com.example.airquality

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class KommuneAdapter(private val kommuneListe: MutableList<Adapter>): RecyclerView.Adapter<KommuneAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var favorittB: CheckBox = view.findViewById(R.id.favorittBoks)
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
        viewHolder.name.text = adapterList[pos].kommuneNavn
        viewHolder.color.setBackgroundColor(Color.parseColor("#${adapterList[pos].fargekode}"))

        viewHolder.favorittB.setOnClickListener {
            if (viewHolder.favorittB.isChecked) {
                favorittList.add(adapterList[pos])
                Log.d("list size after adding:", favorittList.size.toString())
                Log.d("Added", adapterList[pos].kommuneNavn.toString())
            } else {
                favorittList.remove(adapterList[pos])
                Log.d("list size after rm: ", favorittList.size.toString())
                Log.d("Removed", adapterList[pos].kommuneNavn.toString())
            }
        }

        viewHolder.cardView.setOnClickListener {
            val intent = Intent(it.context, CardViewClickActivity::class.java)
            it.context.startActivity(intent)
        }
        viewHolder.name.text = kommuneListe[pos].kommuneNavn
        viewHolder.color.setBackgroundColor(Color.parseColor("#${kommuneListe[pos].fargekode}"))
        viewHolder.weather.text = kommuneListe[pos].vaer.toString()
        viewHolder.weatherValue.text = kommuneListe[pos].beskrivelse.toString()
    }
    override fun getItemCount() = kommuneListe.size
}