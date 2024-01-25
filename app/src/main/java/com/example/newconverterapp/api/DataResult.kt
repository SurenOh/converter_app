package com.example.newconverterapp.api

import com.example.newconverterapp.apimodels.ApiResponseError
import com.example.newconverterapp.apimodels.response.CurrencyResponse

sealed class DataResult<out T: Any> {
    class Success<out T: Any>(val data: T) : DataResult<T>()
    class Failure(val data: ApiResponseError) : DataResult<Nothing>()
    class FailureHttp(val error: String) : DataResult<Nothing>()
}

typealias CurrencyResponseCallback = (DataResult<CurrencyResponse>) -> Unit