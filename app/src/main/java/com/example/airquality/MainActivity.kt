package com.example.airquality

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Create searchview and listener
        val searchView: SearchView = findViewById(R.id.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            // When query is submitted
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()){
                    searchCounty(query, testList)
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
    }

    private fun searchCounty(query: String, testList: AirQualityData) {
        var tempList = mutableListOf<Data>()
        // creates a list of counties which contain the query
        for(i in testList.data){
            if(i.PLACEHOLDER.contains(query, ignoreCase = true)){
                tempList.add(Data(i.PLACEHOLDER))
            }
        }
        val searchList = AirQualityData(tempList)
        // creates the recyclerview
        runOnUiThread {
            var adapter = DataAdapter(searchList.data)
            val recyclerView: RecyclerView = findViewById(R.id.recycle)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            var viewHolder = adapter.createViewHolder(recyclerView, searchList.data.size)
            // Fills the recyclerview
            for(i in searchList.data){
                adapter.bindViewHolder(viewHolder, searchList.data.indexOf(i))
            }
        }
    }
}