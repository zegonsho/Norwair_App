package com.example.airquality

data class Areas(val zone: String?, val municipality: String?, val area: String?, val description: String?)
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
data class Adapter(val kommuneNavn: String?, val fargekode: String?)