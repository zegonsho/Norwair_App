package com.example.airquality

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking

//API
data class Areas(val zone: String?, val municipality: String?, val area: String?, val description: String?)

//Api
data class Aqis(val index: Number?, val fromValue: Number?, val toValue: Number?, val color: String?, val text: String?, val shortDescription: String?, val description: String?, val advice: String?)
data class Statistics(val component: String?, val unit: String?, val aqis: List<Aqis>?)

data class Stasjon(
    val id: Number?,
    val zone: String?,
    val municipality: String?,
    val area: String?,
    val station: String?,
    val eoi: String?,
    val type: String?,
    val component: String?,
    val fromTime: String?,
    val toTime: String?,
    val value: Number?,
    val unit: String?,
    val latitude: Number?,
    val longitude: Number?,
    val timestep: Number?,
    val index: Number?,
    val color: String?,
    val isValid: Boolean?)

//Main
data class Adapter(val kommuneNavn: String?, val fargekode: String?)
//data class Statistics(val text: String?, val shortDescription: String?, val description: String?, val advice: String?)
data class Luftkvalitet(val stasjon: String?, val verdinavn: String?, val verdi: String?)
data class BaseLuftkvalitet(val Liste: MutableList<Luftkvalitet>)
lateinit var stasjonArray: Array<Stasjon>
lateinit var areasArray: Array<Areas>

val adapterList = mutableListOf<Adapter>()
lateinit var valgtKommune: Adapter


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