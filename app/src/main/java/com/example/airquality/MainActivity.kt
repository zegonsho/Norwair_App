package com.example.airquality

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//API
lateinit var valgtKommune: Adapter

class MainActivity : AppCompatActivity() {
    lateinit var stasjonArray: Array<Stasjon>
    lateinit var areasArray: Array<Areas>
    //NILU:
    private val niluStasjonsdataPM10 = "https://api.nilu.no/aq/utd?&components=pm10"
    private val niluLookupAreas = "https://api.nilu.no/lookup/areas"
    private val apiProxyIN2000 = "https://in2000-apiproxy.ifi.uio.no/weatherapi/nowcast/2.0/complete?"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Create air quality data
        val gson = Gson()
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("first: ", "PASSED")
            try {
                // Create recyclerview and adapter
                stasjonArray = gson.fromJson(Fuel.get(niluStasjonsdataPM10).awaitString(), Array<Stasjon>::class.java)
                Log.d("TEST1: ", stasjonArray.size.toString())
                areasArray = gson.fromJson(Fuel.get(niluLookupAreas).awaitString(), Array<Areas>::class.java)
                Log.d("TEST2: ", areasArray.size.toString())
                var vaer = ""
                var vaerBeskrivelse = ""
                var pm10Max = 0f
                var stasjonMedHoyestePM10Maaling: Stasjon
                var c = 0
                val adapterArray: Array<Adapter?> = arrayOfNulls(areasArray.size)

                for (i in areasArray.indices) {
                    for (dataStasjon in stasjonArray) {
                        if ((areasArray[i].area == dataStasjon.area)) {
                            try {
                                if (c == 0) {
                                    val tempRespone = gson.fromJson(Fuel.get(apiProxyIN2000 + "lat=${dataStasjon.latitude.toString()}" + "&lon=${dataStasjon.longitude.toString()}").awaitString(), Base::class.java)
                                    val list = tempRespone.properties?.timeseries
                                    vaer = list?.get(0)?.data?.instant?.details?.air_temperature.toString() + "°C"
                                    vaerBeskrivelse = list?.get(0)?.data?.next_1_hours?.summary?.symbol_code.toString()
                                    c++
                                }
                            } catch (e: Exception) {
                                vaer = "Ingen data"
                                vaerBeskrivelse = "Ingen data"
                            }
                            stasjonMedHoyestePM10Maaling = dataStasjon
                            if (dataStasjon.value?.toFloat() ?: 0f > pm10Max) {
                                pm10Max = dataStasjon.value?.toFloat() ?: 0f
                                stasjonMedHoyestePM10Maaling = dataStasjon
                            }

                            val temp = Adapter(areasArray[i].area, stasjonMedHoyestePM10Maaling.color, vaer, vaerBeskrivelse)
                            adapterArray[i] = temp
                        }
                    }
                    c=0
                }

                for (x in adapterArray) {
                    if (x != null) {
                        adapterList.add(x)
                    }
                }

            } catch (e: Exception) {
                Log.e("Error ", e.message.toString())
            }
        }
        runOnUiThread{
            Log.d("Adapter_start", "success")
            var adapter = KommuneAdapter(adapterList)
            val recyclerView: RecyclerView = findViewById(R.id.recycle)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        }
        // Create searchview and listener
        val searchView: SearchView = findViewById(R.id.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            // When query is submitted
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()){
                    searchCounty(query, adapterList)
                }
                val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboard.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                return true
            }
            // When query is changed
            override fun onQueryTextChange(query: String?): Boolean {
                if (query.equals("")){
                    runOnUiThread{
                        var adapter = KommuneAdapter(adapterList)
                        val recyclerView: RecyclerView = findViewById(R.id.recycle)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    }
                    val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    keyboard.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                }
                return true
            }
        })
        var bottomNavigation : BottomNavigationView = findViewById(R.id.bottom_navigation) //might have to be initalized before
        val navigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            //TODO: implement fragment pages
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
    }
    // Search function
    private fun searchCounty(query: String, list: MutableList<Adapter>) {
        var searchList = mutableListOf<Adapter>()
        Log.d("query", query)
        // creates a list of counties which contain the query
        for (i in list) {
            if (i.kommuneNavn!!.contains(query, ignoreCase = true)) {
                searchList.add(Adapter(i.kommuneNavn, i.fargekode, i.vaer, i.beskrivelse))
            }
        }
        // creates the recyclerview
        runOnUiThread {
            var adapter = KommuneAdapter(searchList)
            val recyclerView: RecyclerView = findViewById(R.id.recycle)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
}