package com.example.newconverterapp.apimodels.response

data class CurrencyResponse(
    val base: String?,
    val date: String?,
    val rates: Map<String?, Double?>?
)