package com.example.newconverterapp.model

data class CurrencyModel(
    val base: String?,
    val rates: List<Rate>
)

data class Rate(
    val code: String,
    var currency: Double
)
