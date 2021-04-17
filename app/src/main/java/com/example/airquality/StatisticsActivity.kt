package com.example.airquality

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
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

        lateinit var descriptionArray: Array<Statistics>
        val lookupAqis = "https://api.nilu.no/lookup/aqis?component="
        val description: TextView = findViewById(R.id.description)

        //Bar Chart
        //https://api.nilu.no/obs/utd?areas={kommune}&components={aqis}

        //Spinner
        val lookupStations = "https://api.nilu.no/lookup/stations?area="

        runBlocking {
            try {
                //Baserer anbefaling som vi gir ved valgt kommune på pm10 verdiene som er der
                descriptionArray = Gson().fromJson(Fuel.get(lookupAqis+"pm10").awaitString(), Array<Statistics>::class.java)

                for (x in descriptionArray) {
                    for (y in x.aqis!!) {
                        if (y.color.toString() == valgtKommune.fargekode) {
                            description.text = y.advice
                            break
                        }
                    }
                }

                stasjonerValgtKommune = Gson().fromJson(Fuel.get(lookupStations+ valgtKommune.kommuneNavn?.toLowerCase()).awaitString(), Array<StasjonerValgtKommune>::class.java)

                stasjonsNavn.add("Generelt")
                for (element in stasjonerValgtKommune) {
                    stasjonsNavn.add(element.station.toString())
                }

                for ((counter, value) in aqisList.withIndex()) {
                    xValues.add(value)
                    barEntries.add(BarEntry(0f, counter))

                    val response =  Gson().fromJson(Fuel.get(url+"areas=${valgtKommune.kommuneNavn}"+"&components=${value}").awaitString(), Array<Stasjon>::class.java)
                    for (data in response) {
                        val temp = Luftkvalitet(data.station, value, data.value.toString())
                        resultatAqis.add(temp)
                    }
                }

            } catch (e : Exception) {
                Log.d("Error fill aqis ", e.toString())
            }
        }

        val spinner: Spinner = findViewById(R.id.spinner)
        spinner.onItemSelectedListener = this
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, stasjonsNavn)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = aa
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //TODO: onNothingSelected()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val toast = Toast.makeText(applicationContext, "Klikk på bar charten for å oppdatere den",Toast.LENGTH_LONG)
        //toast.setGravity(Gravity.CENTER, 0,0)
        toast.show()

        val barChart: BarChart = findViewById(R.id.barchart)

        //Dersom "Generelt" velges tas all data tilgjengelig fra alle målestasjonene og fylles opp på gitte kommune
        if (stasjonsNavn[position] == "Generelt") {

            for (data in resultatAqis) {
                for (i in aqisList.indices) {
                    if (data.verdinavn.toString() == aqisList[i]) {
                        if (barEntries[i].`val` <= data.verdi?.toFloat() ?: 0f) {
                            barEntries[i].`val` = data.verdi?.toFloat() ?: 0f
                            break
                        }
                    }
                }
            }
        } else {

            try {
                //Resetter alle målingene til 0
                for (i in aqisList.indices) {
                    barEntries[i].`val` = 0f
                }

                //Fyller opp bar chart med målingene fra valgt stasjon fra spinner
                for (data in resultatAqis) {

                    if (data.stasjon.toString() == (stasjonsNavn[position])) {
                        for (i in aqisList.indices) {
                            if (data.verdinavn.toString() == aqisList[i]) {
                                barEntries[i].`val` = data.verdi?.toFloat() ?: 0f
                                break
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("error spinner", e.toString())
            }
        }

        //Lager bar charten her
        val barDataSet = BarDataSet(barEntries, "Values")
        barDataSet.valueTextColor = Color.BLACK

        barDataSet.setColors(ColorTemplate.LIBERTY_COLORS)

        val data = BarData(xValues, barDataSet)
        data.setValueTextColor(Color.BLACK)
        barChart.data = data
        barChart.xAxis.setLabelsToSkip(0)
    }
}