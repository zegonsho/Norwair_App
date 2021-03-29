package com.example.airquality

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking

//API
data class KommuneHolder(val kommuneNavn: String?, val kommunenavnNorsk: String?, val kommunenummer: String?)
data class KommuneListe (val kommuner: MutableList<KommuneHolder>)
data class FylkeHolder (val fylkesnavn: String?, val fylkesnummer: String?)
data class FylkeListe (val fylker: MutableList<FylkeHolder>)
//API

data class Kommuner (val kommuneNavn: String?, val fylkesnavn: String?)


class MainActivity : AppCompatActivity() {

    val kommuneString = mutableListOf<String>()
    val kommuneList = mutableListOf<Kommuner>()
    val gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Create searchview and listener
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
        })

        runBlocking {
            try {
                val baseURL = "https://ws.geonorge.no/kommuneinfo/v1/fylker"
                val kommuneURL = "/kommuner"
                val fylkeURL = "/fylker"

                val responseKommuner = gson.fromJson(Fuel.get(baseURL + kommuneURL).awaitString(), KommuneListe::class.java)
                val responseFylker= gson.fromJson(Fuel.get(baseURL+fylkeURL).awaitString(), FylkeListe::class.java)
                Log.d("result: ", responseKommuner.toString())


                val size = responseKommuner.kommuner.size
                val sizeOfFylke = responseFylker.fylker.size
                for (i in 0..size) {
                    val data = responseKommuner.kommuner[i]
                    val sub = data.kommunenummer?.substring(0,2)
                    for (j in 0..sizeOfFylke) {
                        val curFylke = responseFylker.fylker[j]
                        if (sub == curFylke.fylkesnummer) {
                            val newKommune = Kommuner(data.kommuneNavn, curFylke.fylkesnavn)
                            kommuneList.add(newKommune)
                            kommuneString.add(data.kommuneNavn + " / " + curFylke.fylkesnavn)
                            break
                        }
                    }
                }
                Log.d("result: ", kommuneString.toString())


            }
            catch (e: Exception) {
                println(e.message)
            }
        }

    }

    private fun searchCounty(query: String, testList: MutableList<Kommuner>) {
        var searchList = mutableListOf<Kommuner>()
        // creates a list of counties which contain the query
        for(i in testList){
            if(i.kommuneNavn?.contains(query, ignoreCase = true)!!){
                searchList.add(Kommuner(i.kommuneNavn, ""))
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
    }
}