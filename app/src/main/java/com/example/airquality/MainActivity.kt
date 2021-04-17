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

lateinit var valgtKommune: Adapter
lateinit var stasjonerValgtKommune: Array<StasjonerValgtKommune>


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

                areasArray = gson.fromJson(Fuel.get(niluLookupAreas).awaitString(), Array<Areas>::class.java)

                var vaer: String
                var vaerBeskrivelse: String

                for (dataAreas in areasArray) {
                    for (dataStasjon in stasjonArray) {
                        if (dataAreas.area == dataStasjon.area) {
                            try {
                                val tempRespone = gson.fromJson(Fuel.get(apiProxyIN2000 + "lat=${dataStasjon.latitude.toString()}" + "&lon=${dataStasjon.longitude.toString()}").awaitString(), Base::class.java)

                                val list = tempRespone.properties?.timeseries
                                vaer = list?.get(0)?.data?.instant?.details?.air_temperature.toString()
                                vaerBeskrivelse = list?.get(0)?.data?.next_1_hours?.summary?.symbol_code.toString()

                            } catch (e: Exception) {
                                vaer = "Ingen data"
                                vaerBeskrivelse = "Ingen data"
                            }

                            val temp = Adapter(dataAreas.area, dataStasjon.color, vaer, vaerBeskrivelse)
                            adapterList.add(temp)
                            break
                        }
                    }
                }
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