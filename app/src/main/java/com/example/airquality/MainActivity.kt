package com.example.airquality

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
data class KommuneListe (val kommuner: MutableList<KommuneHolder>)
data class FylkeHolder (val fylkesnavn: String?, val fylkesnummer: String?)
data class FylkeListe (val fylker: MutableList<FylkeHolder>)
//API
data class Kommuner (val kommuneNavn: String?, val fylkesnavn: String?)


class MainActivity : AppCompatActivity() {

    private val kommuneString = mutableListOf<String>()
    private val kommuneList = mutableListOf<Kommuner>()
    private val gson = Gson()
    lateinit var recycle: RecyclerView


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
                val baseURL = "https://ws.geonorge.no/kommuneinfo/v1"
                val kommuneURL = "/kommuner"
                val fylkeURL = "/fylker"

                val responseKommuner = gson.fromJson(Fuel.get(baseURL + kommuneURL).awaitString(), Array<KommuneHolder>::class.java)
                Log.d("kommunesize: ", responseKommuner.size.toString())
                val responseFylker= gson.fromJson(Fuel.get(baseURL+fylkeURL).awaitString(), Array<FylkeHolder>::class.java)
                Log.d("fylkesize: ", responseFylker.size.toString())


                val sizeOfKommune = responseKommuner.size - 1
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
                Log.d("result: ", kommuneString.toString())


            }
            catch (e: Exception) {
                Log.e("Error ", e.message.toString())
            }
        }

        recycle = findViewById(R.id.recycle)
        recycle.adapter = KommuneAdapter(kommuneString)
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