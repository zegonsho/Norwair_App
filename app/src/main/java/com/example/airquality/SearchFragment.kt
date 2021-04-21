package com.example.airquality

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var viewOfLayout: View
    lateinit var stasjonArray: Array<Stasjon>
    lateinit var areasArray: Array<Areas>
    //NILU:
    private val niluStasjonsdataPM10 = "https://api.nilu.no/aq/utd?&components=pm10"
    private val niluLookupAreas = "https://api.nilu.no/lookup/areas"
    private val apiProxyIN2000 = "https://in2000-apiproxy.ifi.uio.no/weatherapi/nowcast/2.0/complete?"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_search, container, false)
        // Create air quality data
        val gson = Gson()
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("first: ", "PASSED")
            try {
                // Create recyclerview and adapter
                stasjonArray = gson.fromJson(Fuel.get(niluStasjonsdataPM10).awaitString(), Array<Stasjon>::class.java)
                Log.d("TEST1: ", stasjonArray.size.toString())
                areasArray = gson.fromJson(Fuel.get(niluLookupAreas).awaitString(), Array<Areas>::class.java)
                Log.d("TEST2: ", areasArray.size.toString())
                var vaer: String
                var vaerBeskrivelse: String
                for (dataAreas in areasArray) {
                    for (dataStasjon in stasjonArray) {
                        if (dataAreas.area == dataStasjon.area) {
                            try {
                                val tempRespone = gson.fromJson(Fuel.get(apiProxyIN2000 + "lat=${dataStasjon.latitude.toString()}" + "&lon=${dataStasjon.longitude.toString()}").awaitString(), Base::class.java)
                                val list = tempRespone.properties?.timeseries
                                vaer = list?.get(0)?.data?.instant?.details?.air_temperature.toString() + "°C"
                                vaerBeskrivelse = list?.get(0)?.data?.next_1_hours?.summary?.symbol_code.toString()
                            } catch (e: Exception) {
                                vaer = "Ingen data"
                                vaerBeskrivelse = "Ingen data"
                            }
                            val temp = Adapter(dataAreas.area, dataStasjon.color, vaer, vaerBeskrivelse)
                            adapterList.add(temp)
                            break
                        }
                    }
                }
                updateRecycler(adapterList)
            } catch (e: Exception) {
                Log.e("Error ", e.message.toString())
            }
        }
        // Create searchview and listener
        val searchView: SearchView = viewOfLayout.findViewById(R.id.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            // When query is submitted
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchCounty(query!!, adapterList)
                val keyboard = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboard.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken, 0)
                return true
            }
            // When query is changed
            override fun onQueryTextChange(query: String?): Boolean {
                if (query.isNullOrEmpty()){
                    updateRecycler(adapterList)
                }
                return true
            }
        })
        return viewOfLayout
    }
    // Search function
    private fun searchCounty(query: String, list: MutableList<Adapter>) {
        var searchList = mutableListOf<Adapter>()
        for (i in list) {
            if (i.kommuneNavn!!.contains(query, ignoreCase = true)) {
                searchList.add(Adapter(i.kommuneNavn, i.fargekode, i.vaer, i.beskrivelse))
            }
        }
        updateRecycler(searchList)
    }
    // Update recyclerview
    private fun updateRecycler(list: MutableList<Adapter>){
        activity!!.runOnUiThread {
            var adapter = KommuneAdapter(list)
            val recyclerView: RecyclerView = activity!!.findViewById(R.id.recycle)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this.context)
        }
    }
    companion object {
        // Method to create a new instance of this fragment
        fun newInstance(): SearchFragment{
            return SearchFragment()
        }
    }
}