package com.example.airquality

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class InfoFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var viewOfLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_info, container, false)
        val spinner: Spinner = viewOfLayout.findViewById(R.id.spinnerInfo)

        //lager spinner
        spinner.onItemSelectedListener = this
        val adapter = arrayOf("CO", "NO", "NO2", "NOx", "03", "PM(1/2.5/10)", "SO2")
        val aa = ArrayAdapter(this.context!!, android.R.layout.simple_spinner_item, adapter)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = aa

        return viewOfLayout
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val spinner: Spinner = viewOfLayout.findViewById(R.id.spinnerInfo)
        val title: TextView = viewOfLayout.findViewById(R.id.unit)
        val desc: TextView = viewOfLayout.findViewById(R.id.description)
        val unit = spinner.selectedItem.toString()

        if (unit == "CO") {
            title.text = getString(R.string.CO2)
            desc.text = getString(R.string.CO2desc)

        } else if (unit == "NO") {
            title.text = getString(R.string.NO)
            desc.text = getString(R.string.NOdesc)

        } else if (unit == "NO2") {
            title.text = getString(R.string.NO2)
            desc.text = getString(R.string.NO2desc)

        } else if (unit == "NOx") {
            title.text = getString(R.string.NOx)
            desc.text = getString(R.string.NOxdesc)

        } else if (unit == "03") {
            title.text = getString(R.string.O3)
            desc.text = getString(R.string.O3desc)

        } else if (unit == "PM(1/2.5/10)") {
            title.text = getString(R.string.PM)
            desc.text = getString(R.string.PMdesc)

        } else if (unit == "SO2") {
            title.text = getString(R.string.SO2)
            desc.text = getString(R.string.SO2desc)

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    companion object {
        // Method to create a new instance of this fragment
        fun newInstance(): InfoFragment{
            return InfoFragment()
        }
    }
}