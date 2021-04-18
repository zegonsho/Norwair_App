package com.example.airquality

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking


//API
data class KommuneHolder(val kommunenavn: String?, val kommunenavnNorsk: String?, val kommunenummer: String?)
data class FylkeHolder (val fylkesnavn: String?, val fylkesnummer: String?)

data class MetApiKommune(val name: String?, val path: String?, val longitude: String?, val latitude: String?, val areacode: String?, val areaclass: String?, val superareacode: String?)

//Main
data class Kommuner (val kommuneNavn: String?, val fylkesnavn: String?)

lateinit var stasjonArray: Array<Stasjon>
lateinit var areasArray: Array<Areas>

lateinit var valgtKommune: Adapter


class MainActivity : AppCompatActivity() {

    private val gson = Gson()
    lateinit var recycle: RecyclerView
    //NILU:
    private val niluStasjonsdataPM10 = "https://api.nilu.no/aq/utd?&components=pm10"
    private val niluLookupAreas = "https://api.nilu.no/lookup/areas"
    //https://in2000-apiproxy.ifi.uio.no/weatherapi/nowcast/2.0/complete?lat={}&lon={}
    private val apiProxyIN2000 = "https://in2000-apiproxy.ifi.uio.no/weatherapi/nowcast/2.0/complete?"

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

                stasjonArray = gson.fromJson(Fuel.get(niluStasjonsdataPM10).awaitString(), Array<Stasjon>::class.java)

                areasArray = gson.fromJson(Fuel.get(niluLookupAreas).awaitString(), Array<Areas>::class.java)

                var vaer: String = ""
                var vaerBeskrivelse: String = ""


                for (dataAreas in areasArray) {
                    for (dataStasjon in stasjonArray) {
                        if (dataAreas.area == dataStasjon.area) {
                            val temp = Adapter(dataAreas.area, dataStasjon.color,vaer,vaerBeskrivelse)
                            adapterList.add(temp)
                            break
                        }
                    }
                }

            }
            catch (e: Exception) {
                Log.e("Error ", e.message.toString())
            }
            catch (e: Exception) {
                Log.e("Error Adapterlist", e.message.toString())
            }
        }

        recycle = findViewById(R.id.recycle)
        recycle.adapter = KommuneAdapter(adapterList)
        recycle.layoutManager = LinearLayoutManager(this)
    }
}