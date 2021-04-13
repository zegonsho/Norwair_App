package com.example.airquality

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class StatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        lateinit var statisticsArray: Array<Statistics>
        lateinit var descriptionArray: Array<Statistics>
        val lookupAqis = "https://api.nilu.no/lookup/aqis?component="
        val lookupStasjoner = "https://api.nilu.no/lookup/stations?area="
        val description: TextView = findViewById(R.id.description)
        val by: TextView = findViewById(R.id.by)

        //Bar Chart
        val aqisList = mutableListOf("CO", "NO", "NO2", "NOx", "O3", "PM1", "PM10", "PM2.5", "SO2")
        val resultatAqis = mutableListOf<Luftkvalitet>()
        //https://api.nilu.no/obs/utd?areas={kommune}&components={aqis}
        lateinit var valgtKommuneVerdier: Array<Stasjon>
        val url = "https://api.nilu.no/obs/utd?"

        //Spinner
        lateinit var stasjonsNavn: MutableList<String>
        val lookupStations = "https://api.nilu.no/lookup/stations?area="
        lateinit var spinnerlanguages: Spinner

        runBlocking {
            try {
                descriptionArray = Gson().fromJson(Fuel.get(lookupAqis+"pm10").awaitString(), Array<Statistics>::class.java)
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

                for (value in aqisList) {
                    //statisticsArray =  Gson().fromJson(Fuel.get(lookupAqis+value).awaitString(), Array<Statistics>::class.java)
                    val response =  Gson().fromJson(Fuel.get(url+"areas=${valgtKommune.kommuneNavn}"+"&components=${value}").awaitString(), Array<Stasjon>::class.java)
                    for (data in response) {
                        val temp = Luftkvalitet(data.station, value, data.value.toString())
                        Log.d("station", data.station.toString())
                        Log.d("value", value)
                        Log.d("data", data.value.toString())
                        resultatAqis.add(temp)
                    }
                }

                stasjonerValgtKommune = Gson().fromJson(Fuel.get(lookupStations+ valgtKommune.kommuneNavn?.toLowerCase()).awaitString(), Array<StasjonerValgtKommune>::class.java)
                valgtKommune.kommuneNavn?.toLowerCase()?.let { Log.d("hei   ", it) }
                var size = stasjonerValgtKommune.size
                stasjonsNavn = mutableListOf()
                for (i in 0..stasjonerValgtKommune.size) {
                    stasjonsNavn.add(stasjonerValgtKommune[i].station.toString())
                }




            } catch (e : Exception) {
                Log.d("Error ", e.toString())
            }
        }

        spinnerlanguages = findViewById(R.id.spinner)
        var aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, stasjonsNavn)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerlanguages.adapter = aa
    }
}