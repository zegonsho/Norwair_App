package com.example.airquality

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.*

class StatisticsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        lateinit var spinnerlanguages:Spinner

        lateinit var statisticsArray: Array<Statistics>
        lateinit var descriptionArray: Array<Description>
        lateinit var stasjonsNavn : MutableList<String>
        val lookupAqis = "https://api.nilu.no/lookup/aqis?component=pm10"
        val lookupStations = "https://api.nilu.no/lookup/stations?area="
        val description: TextView = findViewById(R.id.description)
        val by: TextView = findViewById(R.id.by)



        spinnerlanguages = this.findViewById(R.id.spinner)
        spinnerlanguages.onItemSelectedListener = this

        //val spinner: Spinner = findViewById(R.id.spinner)
        //spinner.onItemSelectedListener = this




        runBlocking {
            try {
                descriptionArray = Gson().fromJson(Fuel.get(lookupAqis).awaitString(), Array<Description>::class.java)
                Log.d("TAG", "onCreate: $descriptionArray")
                valgtKommune.kommuneNavn?.let { Log.d("valgt Kommune", it) }
                for (x in descriptionArray) {
                    for (y in x.aqis!!) {
                        if (y.color.toString() == valgtKommune.fargekode) {
                            description.text = y.advice
                            by.text = valgtKommune.kommuneNavn
                            break
                        }
                    }
                }
                stasjonerValgtKommune = Gson().fromJson(Fuel.get(lookupStations+ valgtKommune.kommuneNavn?.toLowerCase()).awaitString(), Array<StasjonerKommune>::class.java)
                valgtKommune.kommuneNavn?.toLowerCase()?.let { Log.d("hei   ", it) }
                var size = stasjonerValgtKommune.size
                stasjonsNavn = mutableListOf()
                for (i in 0..stasjonerValgtKommune.size) {
                    stasjonsNavn.add(stasjonerValgtKommune[i].station.toString())
                }


                Log.d("station array: ", stasjonerValgtKommune.size.toString())


            } catch (e : Exception) {
                Log.d("Error ", e.toString())
            }
        }

        var aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, stasjonsNavn)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerlanguages.adapter = aa

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val by: TextView = findViewById(R.id.by)
        by.text = "Selected: "+ stasjonerValgtKommune[position]
    }
}