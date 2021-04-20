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
import androidx.recyclerview.widget.RecyclerView
import java.util.*



class KommuneAdapter(private val adapterList: MutableList<Adapter>): RecyclerView.Adapter<KommuneAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.PLACEHOLDER)
        var color: View = view.findViewById(R.id.color)
        var cardView: CardView = view.findViewById(R.id.cardView)
        var favorittB: CheckBox = view.findViewById(R.id.favorittBoks)
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
            //Toast.makeText(it.context,"this is toast message",Toast.LENGTH_SHORT).show()

            viewHolder.name.text = adapterList[pos].kommuneNavn
            viewHolder.color.setBackgroundColor(Color.parseColor("#${adapterList[pos].fargekode}"))
            viewHolder.weather.text = adapterList[pos].vaer.toString()
            viewHolder.weatherValue.text = adapterList[pos].beskrivelse.toString()

            viewHolder.cardView.setOnClickListener {
                valgtKommune = adapterList[pos]
                val intent = Intent(it.context, StatisticsActivity::class.java)
                it.context.startActivity(intent)

            }
        }
    }
    override fun getItemCount() = adapterList.size
}