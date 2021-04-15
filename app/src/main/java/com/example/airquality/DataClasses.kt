package com.example.airquality

//API
data class Areas(val zone: String?, val municipality: String?, val area: String?, val description: String?)

//Api
data class Aqis(val index: Number?, val fromValue: Number?, val toValue: Number?, val color: String?, val text: String?, val shortDescription: String?, val description: String?, val advice: String?)
data class Statistics(val component: String?, val unit: String?, val aqis: List<Aqis>?)

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

data class StasjonerValgtKommune(val id: Number?, val zone: String?, val municipality: String?, val area: String?, val station: String?, val type: String?, val eoi: String?, val latitude: Number?, val longitude: Number?, val owner: String?, val status: String?, val description: String?, val firstMeasurment: String?, val lastMeasurment: String?, val components: String?, val isVisible: Boolean?)

//Main
data class Adapter(val kommuneNavn: String?, val fargekode: String?)
//data class Statistics(val text: String?, val shortDescription: String?, val description: String?, val advice: String?)
data class Luftkvalitet(val stasjon: String?, val verdinavn: String?, val verdi: String?)
data class BaseLuftkvalitet(val Liste: MutableList<Luftkvalitet>)

class DataClasses {
}