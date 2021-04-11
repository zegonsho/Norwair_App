package com.example.airquality

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        lateinit var descriptionArray: Array<Description>
        val lookupAqis = "https://api.nilu.no/lookup/aqis?component=pm10"
        val description: TextView = findViewById(R.id.description)
        val by: TextView = findViewById(R.id.by)

        runBlocking {
            try {
                descriptionArray = Gson().fromJson(Fuel.get(lookupAqis).awaitString(), Array<Description>::class.java)
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

            } catch (e : Exception) {
                Log.d("Error ", e.toString())
            }
        }
    }
}