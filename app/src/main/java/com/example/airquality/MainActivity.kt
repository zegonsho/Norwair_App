package com.example.airquality

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking


//API
data class KommuneHolder(val kommunenavn: String?, val kommunenavnNorsk: String?, val kommunenummer: String?)
data class FylkeHolder (val fylkesnavn: String?, val fylkesnummer: String?)
data class Areas(val zone: String?, val municipality: String?, val area: String?, val description: String?)

data class MetApiKommune(val name: String?, val path: String?, val longitude: String?, val latitude: String?, val areacode: String?, val areaclass: String?, val superareacode: String?)



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
data class Kommuner (val kommuneNavn: String?, val fylkesnavn: String?)
data class Adapter(val kommuneNavn: String?, val fargekode: String?)

lateinit var stasjonArray: Array<Stasjon>
lateinit var areasArray: Array<Areas>
val adapterList = mutableListOf<Adapter>()

class MainActivity : AppCompatActivity(){

    private val kommuneString = mutableListOf<String>()
    private val kommuneList = mutableListOf<Kommuner>()
    private val gson = Gson()
    lateinit var recycle: RecyclerView
    //API:
    val niluStasjonsdataPM10 = "https://api.nilu.no/aq/utd?&components=pm10"
    val niluLookupAreas = "https://api.nilu.no/lookup/areas"
    val apiMetKommune = "https://api.met.no/weatherapi/airqualityforecast/0.1/areas?areaclass=kommune"
    val apiMetStasjon = "https://api.met.no/weatherapi/airqualityforecast/0.1/stations"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*// Create searchview and listener
        val searchView: SearchView = findViewById(R.id.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            // When query is submitted
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()){
                    searchCounty(query, kommuneList)
                }
                val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboard.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                return true
            }
            // When query is changed
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })*/

        runBlocking {
            Log.d("first: ", "PASSED")
            try {

                //val testMET = "https://api.met.no/weatherapi/airqualityforecast/0.1/areas?areaclass=kommune"
                //val response = gson.fromJson(Fuel.get(testMET).awaitString(), base::class.java)
                //Log.d("testMet: ", response.toString())

                /*val baseURL = "https://ws.geonorge.no/kommuneinfo/v1"
                val kommuneURL = "/kommuner"
                val fylkeURL = "/fylker"

                val responseKommuner = gson.fromJson(Fuel.get(baseURL + kommuneURL).awaitString(), Array<KommuneHolder>::class.java)
                Log.d("kommunesize: ", responseKommuner.size.toString())
                val responseFylker= gson.fromJson(Fuel.get(baseURL+fylkeURL).awaitString(), Array<FylkeHolder>::class.java)
                Log.d("fylkesize: ", responseFylker.size.toString())*/

                stasjonArray = gson.fromJson(Fuel.get(niluStasjonsdataPM10).awaitString(), Array<Stasjon>::class.java)
                Log.d("TEST1: ", stasjonArray.size.toString())

                areasArray = gson.fromJson(Fuel.get(niluLookupAreas).awaitString(), Array<Areas>::class.java)
                Log.d("TEST2: ", areasArray.size.toString())

                /*val response =  gson.fromJson(Fuel.get(apiMetKommune).awaitString(), Array<MetApiKommune>::class.java)
                Log.d("TEST3: ", response.size.toString())*/

                for (dataAreas in areasArray) {
                    for (dataStasjon in stasjonArray) {
                        if (dataAreas.area == dataStasjon.area) {
                            val temp = Adapter(dataAreas.area, dataStasjon.color)
                            adapterList.add(temp)
                            break
                        }
                    }
                }


              /*  val sizeOfKommune = responseKommuner.size - 1
                val sizeOfFylke = responseFylker.size - 1
                for (i in 0..sizeOfKommune) {
                    val curKommune = responseKommuner[i]
                    val sub = curKommune.kommunenummer?.substring(0,2)
                    for (j in 0..sizeOfFylke) {
                        val curFylke = responseFylker[j]
                        if (sub == curFylke.fylkesnummer) {
                            val newKommune = Kommuner(curKommune.kommunenavn, curFylke.fylkesnavn)
                            kommuneList.add(newKommune)
                            kommuneString.add(curKommune.kommunenavn + " / " + curFylke.fylkesnavn)
                            break
                        }
                    }
                }
                Log.d("result: ", kommuneString.toString())*/


            }
            catch (e: Exception) {
                Log.e("Error ", e.message.toString())
            }
        }

        recycle = findViewById(R.id.recycle)
        recycle.adapter = KommuneAdapter(adapterList)
        recycle.layoutManager = LinearLayoutManager(this)

    }



    /*private fun searchCounty(query: String, testList: MutableList<Kommuner>) {
        var searchList = mutableListOf<Kommuner>()
        // creates a list of counties which contain the query
        for(i in testList){
            if(i.kommuneNavn?.contains(query, ignoreCase = true)!!){
                searchList.add(Kommuner(i.kommuneNavn, i.fylkesnavn))
            }
        }
        //val searchList = KommuneListe(tempList)
        // creates the recyclerview
        runOnUiThread {
            var adapter = KommuneAdapter(searchList)
            val recyclerView: RecyclerView = findViewById(R.id.recycle)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            var viewHolder = adapter.createViewHolder(recyclerView, searchList.size)
            // Fills the recyclerview
            for(i in searchList){
                adapter.bindViewHolder(viewHolder, searchList.indexOf(i))
            }
        }
    }*/

}