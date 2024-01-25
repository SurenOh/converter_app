package com.example.newconverterapp.repository.currency

interface CurrencyRepository {
    fun getCurrency(completionHandler: GetCurrencyDataCallback)
}