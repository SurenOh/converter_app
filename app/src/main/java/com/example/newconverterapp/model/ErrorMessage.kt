package com.example.newconverterapp.model

enum class ErrorMessage(val message: String) {
    FillFields("Please fill out all fields for conversion"),
    NotEnoughMoney("Sorry, but you don't have enough money to convert!"),
    SameCurrencies("Please select different currencies for conversion!"),
    IncorrectValue("Please type correct value!"),
    Unknown("Error! Please try again later!"),
    NoInternet("Internet is unavailable!"),
    ServerError("Server error!")
}