package com.example.airquality

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class StatisticsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        lateinit var statisticsArray: Array<Statistics>
        lateinit var descriptionArray: Array<Statistics>
        val lookupAqis = "https://api.nilu.no/lookup/aqis?component="
        val lookupStasjoner = "https://api.nilu.no/lookup/stations?area="
        val description: TextView = findViewById(R.id.description)

        //Bar Chart
        //https://api.nilu.no/obs/utd?areas={kommune}&components={aqis}
        lateinit var valgtKommuneVerdier: Array<Stasjon>

        //Spinner
        //lateinit var stasjonsNavn: MutableList<String>
        val lookupStations = "https://api.nilu.no/lookup/stations?area="

        runBlocking {
            try {
                descriptionArray = Gson().fromJson(Fuel.get(lookupAqis+"pm10").awaitString(), Array<Statistics>::class.java)
                Log.d("TAG", "onCreate: $descriptionArray")
                valgtKommune.kommuneNavn?.let { Log.d("valgt Kommune", it) }

                for (x in descriptionArray) {
                    for (y in x.aqis!!) {
                        if (y.color.toString() == valgtKommune.fargekode) {
                            description.text = y.advice
                            break
                        }
                    }
                }

                stasjonerValgtKommune = Gson().fromJson(Fuel.get(lookupStations+ valgtKommune.kommuneNavn?.toLowerCase()).awaitString(), Array<StasjonerValgtKommune>::class.java)
                valgtKommune.kommuneNavn?.toLowerCase()?.let { Log.d("hei   ", it) }
                var size = stasjonerValgtKommune.size
                //stasjonsNavn = mutableListOf()
                for (i in 0..stasjonerValgtKommune.size) {
                    stasjonsNavn.add(stasjonerValgtKommune[i].station.toString())
                }

                for (value in aqisList) {
                    //statisticsArray =  Gson().fromJson(Fuel.get(lookupAqis+value).awaitString(), Array<Statistics>::class.java)
                    val response =  Gson().fromJson(Fuel.get(url+"areas=${valgtKommune.kommuneNavn}"+"&components=${value}").awaitString(), Array<Stasjon>::class.java)
                    for (data in response) {
                        val temp = Luftkvalitet(data.station, value, data.value.toString())
                        Log.d("station", data.station.toString())
                        Log.d("value", value)
                        Log.d("data", data.value.toString())
                        resultatAqis.add(temp)
                        //data.value?.toFloat()?.let { BarEntry(value.toFloat(), it) }?.let { resultValues.add(it) }
                        break

                    }
                }




            } catch (e : Exception) {
                Log.d("Error ", e.toString())
            }
        }

        var spinnerlanguages: Spinner = findViewById(R.id.spinner)
        spinnerlanguages.onItemSelectedListener = this
        var aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, stasjonsNavn)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerlanguages.adapter = aa
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val barChart: BarChart = findViewById(R.id.barchart)
        val resultValues = ArrayList<BarEntry>()
        val r = Random()
        val it = (1 + r.nextFloat() * (50 - 1))

        resultValues.add(BarEntry(2015f, it))
        resultValues.add(BarEntry(2016f, 110f))
        resultValues.add(BarEntry(2017f, 150f))
        resultValues.add(BarEntry(2018f, 100f))
        resultValues.add(BarEntry(2019f, 110f))
        resultValues.add(BarEntry(2020f, it))

        val barDataSet = BarDataSet(resultValues, "Result")
        //barDataSet.colors = Color.GREEN
        barDataSet.valueTextColor = Color.BLACK

        val barData = BarData(barDataSet)
        barChart.setFitBars(true)
        barChart.data = barData


        Log.d("name", stasjonsNavn[position])

    }
}