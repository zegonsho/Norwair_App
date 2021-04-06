package com.example.airquality

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
data class Stasjon(val id: Number?, val zone: String?, val municipality: String?, val area: String?, val station: String?, val eoi: String?, val type: String?, val component: String?, val fromTime: String?, val toTime: String?, val value: Number?, val unit: String?, val latitude: Number?, val longitude: Number?, val timestep: Number?, val index: Number?, val color: String?, val isValid: Boolean?)


class CardViewClickActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_view_click)

        val stasjonsdataURL = "https://api.nilu.no/aq/utd?&components=pm10"
    }
}