package com.example.newconverterapp.network.currency

import com.example.newconverterapp.api.CurrencyResponseCallback
import com.example.newconverterapp.api.currency.CurrencyApi
import com.example.newconverterapp.network.ApiService
import com.example.newconverterapp.network.DataFetcher

class CurrencyDataFetcher(private val apiService: ApiService) : DataFetcher() {

    fun getCurrencyData(completionHandler: CurrencyResponseCallback) {
        apiService.procedure(
            api = CurrencyApi.GetCurrencyData,
            completionHandler = callback(completionHandler)
        )
    }

}