package com.example.newconverterapp.api.currency

import com.example.newconverterapp.api.API
import com.example.newconverterapp.api.ApiRequestType
import com.example.newconverterapp.api.ApiValue

class CurrencyApi {
    object GetCurrencyData : API( ApiValue("tasks/api/currency-exchange-rates", ApiRequestType.Get))
}