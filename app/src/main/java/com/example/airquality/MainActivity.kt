package com.example.airquality

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.github.mikephil.charting.data.BarEntry
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking


lateinit var stasjonArray: Array<Stasjon>
lateinit var areasArray: Array<Areas>

val adapterList = mutableListOf<Adapter>()
lateinit var valgtKommune: Adapter
lateinit var stasjonerValgtKommune: Array<StasjonerValgtKommune>

//statistikk
val stasjonsNavn = mutableListOf<String>()
val aqisList = mutableListOf("CO", "NO", "NO2", "NOx", "O3", "PM1", "PM10", "PM2.5", "SO2")
const val url = "https://api.nilu.no/obs/utd?"
val resultatAqis = mutableListOf<Luftkvalitet>()
val xValues = ArrayList<String>()
val barEntries = ArrayList<BarEntry>()



class MainActivity : AppCompatActivity() {

    private val gson = Gson()
    lateinit var recycle: RecyclerView
    //NILU:
    val niluStasjonsdataPM10 = "https://api.nilu.no/aq/utd?&components=pm10"
    val niluLookupAreas = "https://api.nilu.no/lookup/areas"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runBlocking {
            Log.d("first: ", "PASSED")
            try {
                stasjonArray = gson.fromJson(Fuel.get(niluStasjonsdataPM10).awaitString(), Array<Stasjon>::class.java)
                Log.d("TEST1: ", stasjonArray.size.toString())

                areasArray = gson.fromJson(Fuel.get(niluLookupAreas).awaitString(), Array<Areas>::class.java)
                Log.d("TEST2: ", areasArray.size.toString())

                for (dataAreas in areasArray) {
                    for (dataStasjon in stasjonArray) {
                        if (dataAreas.area == dataStasjon.area) {
                            val temp = Adapter(dataAreas.area, dataStasjon.color)
                            adapterList.add(temp)
                            break
                        }
                    }
                }
            }
            catch (e: Exception) {
                Log.e("Error ", e.message.toString())
            }
        }

        recycle = findViewById(R.id.recycle)
        recycle.adapter = KommuneAdapter(adapterList)
        recycle.layoutManager = LinearLayoutManager(this)
    }
}