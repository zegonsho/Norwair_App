package com.example.airquality

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking


//Main:

//API
data class Areas(val zone: String?, val municipality: String?, val area: String?, val description: String?)

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

lateinit var stasjonArray: Array<Stasjon>
lateinit var areasArray: Array<Areas>

val adapterList = mutableListOf<Adapter>()

class MainActivity : AppCompatActivity() {

    private val gson = Gson()
    lateinit var recycle: RecyclerView

    //NILU:
    val niluStasjonsdataPM10 = "https://api.nilu.no/aq/utd?&components=pm10"
    val niluLookupAreas = "https://api.nilu.no/lookup/areas"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var bottomNavigation : BottomNavigationView = findViewById(R.id.bottom_navigation) //might have to be initalized before
        val navigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            //TODO: implement fragement pages
            when (item.itemId) {
                R.id.navigation_search -> {
                    //openFragment(HomeFragment.newInstance("", ""))
                    //return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_fav -> {
                    //openFragment(SmsFragment.newInstance("", ""))
                    //return@OnNavigationItemSelectedListener true

                }
                R.id.navigation_info -> {
                    //openFragment(NotificationFragment.newInstance("", ""))
                    //return@OnNavigationItemSelectedListener true

                }
            }
            false
        }

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