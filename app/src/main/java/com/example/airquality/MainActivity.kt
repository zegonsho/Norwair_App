package com.example.airquality

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val gson = Gson()
    lateinit var recycle: RecyclerView
    lateinit var stasjonArray: Array<Stasjon>
    lateinit var areasArray: Array<Areas>
    val adapterList = mutableListOf<Adapter>()
    //NILU:
    val niluStasjonsdataPM10 = "https://api.nilu.no/aq/utd?&components=pm10"
    val niluLookupAreas = "https://api.nilu.no/lookup/areas"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Create air quality data
        val gson = Gson()
        // ENTIRE COROUTINESCOPE IS JUST A PLACEHOLDER (THE CODE WON'T RUN THROUGH IT RIGHT NOW)
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("first: ", "PASSED")
            try {
                // Create recyclerview and adapter
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
            } catch (e: Exception) {
                Log.e("Error ", e.message.toString())
            }
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
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        // Button to map
        val buttonMap: Button = findViewById(R.id.button_map)
        buttonMap.setOnClickListener{
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
        // Button to info
        val buttonInfo: Button = findViewById(R.id.button_info)
        buttonInfo.setOnClickListener{
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }
    }
    // Search function
    private fun searchCounty(query: String, list: MutableList<Adapter>) {
        var searchList = mutableListOf<Adapter>()
        // creates a list of counties which contain the query
        for (i in list) {
            if (i.kommuneNavn!!.contains(query, ignoreCase = true)) {
                searchList.add(Adapter(i.kommuneNavn, i.fargekode))
            }
        }
        // creates the recyclerview
        runOnUiThread {
            var adapter = KommuneAdapter(searchList)
            val recyclerView: RecyclerView = findViewById(R.id.recycle)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            var viewHolder = adapter.createViewHolder(recyclerView, searchList.size)
            // Fills the recyclerview
            for (i in searchList) {
                adapter.bindViewHolder(viewHolder, searchList.indexOf(i))
            }
        }
    }
}