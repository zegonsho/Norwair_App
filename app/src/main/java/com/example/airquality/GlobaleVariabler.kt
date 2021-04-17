package com.example.airquality

import com.github.mikephil.charting.data.BarEntry


//Main
val adapterList = mutableListOf<Adapter>()


//statistikk
val stasjonsNavn = mutableListOf<String>()
val aqisList = mutableListOf("CO", "NO", "NO2", "NOx", "O3", "PM1", "PM10", "PM2.5", "SO2")
const val url = "https://api.nilu.no/obs/utd?"
val resultatAqis = mutableListOf<Luftkvalitet>()
val xValues = ArrayList<String>()
val barEntries = ArrayList<BarEntry>()