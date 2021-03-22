package com.example.airquality

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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
}