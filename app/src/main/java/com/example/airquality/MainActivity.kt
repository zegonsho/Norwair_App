package com.example.airquality

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import kotlinx.coroutines.runBlocking




data class KommuneHolder(val kommuneNavn: String?, val kommunenavnNorsk: String?, val kommunenummer: String?)
data class KommuneListe (val kommuner: MutableList<KommuneHolder>)

data class FylkeHolder (val fylkesnavn: String?, val fylkesnummer: String?)
data class FylkeListe (val fylker: MutableList<FylkeHolder>)
data class Kommuner (val kommuneNavn: String?, val fylkesnavn: String?)




val kommuneString = mutableListOf<String>()
val kommuneList = mutableListOf<Kommuner>()



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_kommune)

        val gson = Gson()
        Log.d("test", "test")

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

}
