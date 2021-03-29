package com.example.airquality

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Create air quality data
        val gson = Gson()
        // ENTIRE COROUTINESCOPE IS JUST A PLACEHOLDER (THE CODE WON'T RUN THROUGH IT RIGHT NOW)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val airQualityList = gson.fromJson(
                        Fuel.get(
                                "https://api.met.no/weatherapi/airqualityforecast/0.1/swagger.json"
                        ).awaitString(), AirQualityData::class.java
                )
                // Create recyclerview and adapter
                runOnUiThread {
                    var adapter = DataAdapter(airQualityList.data)
                    val recyclerView: RecyclerView = findViewById(R.id.recycle)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    var viewHolder = adapter.createViewHolder(recyclerView, airQualityList.data.size)
                    // Fills the recyclerview
                    for(i in airQualityList.data){
                        adapter.bindViewHolder(viewHolder, airQualityList.data.indexOf(i))
                    }
                }
            } catch (exception: Exception){
                println("A network request exception was thrown: ${exception.message}")
            }
        }
        // Placeholder county list
        var countyList = mutableListOf<Data>()
        countyList.add(Data("Oslo"))
        countyList.add(Data("Rogaland"))
        countyList.add(Data("Møre og Romsdal"))
        countyList.add(Data("Nordaland"))
        countyList.add(Data("Viken"))
        countyList.add(Data("Innlandet"))
        countyList.add(Data("Vestfold og Telemark"))
        countyList.add(Data("Agder"))
        countyList.add(Data("Vestland"))
        countyList.add(Data("Trøndelag"))
        countyList.add(Data("Troms og Finnmark"))
        val testList = AirQualityData(countyList)
        // Create searchview and listener
        val searchView: SearchView = findViewById(R.id.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            // When query is submitted
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()){
                    searchCounty(query, testList)
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
    private fun searchCounty(query: String, testList: AirQualityData) {
        var tempList = mutableListOf<Data>()
        // creates a list of counties which contain the query
        for(i in testList.data){
            if(i.PLACEHOLDER.contains(query, ignoreCase = true)){
                tempList.add(Data(i.PLACEHOLDER))
            }
        }
        val searchList = AirQualityData(tempList)
        // creates the recyclerview
        runOnUiThread {
            var adapter = DataAdapter(searchList.data)
            val recyclerView: RecyclerView = findViewById(R.id.recycle)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            var viewHolder = adapter.createViewHolder(recyclerView, searchList.data.size)
            // Fills the recyclerview
            for(i in searchList.data){
                adapter.bindViewHolder(viewHolder, searchList.data.indexOf(i))
            }
        }
    }
}