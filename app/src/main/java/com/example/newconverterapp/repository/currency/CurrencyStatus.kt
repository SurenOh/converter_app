package com.example.newconverterapp.repository.currency

import com.example.newconverterapp.model.CurrencyModel

sealed class CurrencyStatus {
    class StatusError(val error: String) : CurrencyStatus()
    class StatusErrorHttp(val error: String) : CurrencyStatus()
    class StatusSuccess(val model: CurrencyModel) : CurrencyStatus()
}
