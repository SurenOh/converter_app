package com.example.newconverterapp.model

data class ConversionModel (
    val sell: ConversionItem,
    val receive: ConversionItem
)

data class ConversionItem(
    val value: Double,
    val code: String
)