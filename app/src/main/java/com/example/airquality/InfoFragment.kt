package com.example.airquality

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class InfoFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var viewOfLayout: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_info, container, false)
        val spinner: Spinner = viewOfLayout.findViewById(R.id.spinnerInfo)

        spinner.onItemSelectedListener = this
        val adapter = arrayOf("CO", "NO", "NO2", "NOx", "03", "PM1", "PM10", "PM2.5", "SO2")
        val aa = ArrayAdapter(this.context!!, android.R.layout.simple_spinner_item, adapter)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = aa

        return viewOfLayout





    }



    companion object {
        // Method to create a new instance of this fragment
        fun newInstance(): InfoFragment{
            return InfoFragment()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val spinner: Spinner = viewOfLayout.findViewById(R.id.spinnerInfo)
        val title :TextView = viewOfLayout.findViewById(R.id.unit)
        val desc :TextView = viewOfLayout.findViewById(R.id.description)
        var unit = spinner.selectedItem.toString()
        if (unit == "CO") {
            title.text = "Karbondioksid (CO2)"
            desc.text = ""
        }
    }
}