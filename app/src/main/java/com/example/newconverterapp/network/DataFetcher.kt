package com.example.newconverterapp.network

import com.example.newconverterapp.api.ApiResult
import com.example.newconverterapp.api.DataResult
import com.example.newconverterapp.apimodels.ApiResponseError
import com.google.gson.Gson

open class DataFetcher {

    internal inline fun <reified T: Any> callback(crossinline completionHandler: (DataResult<T>) -> Unit): ApiResponse {
        return { apiResult ->
            val result = when (apiResult) {
                is ApiResult.FailureHttp -> DataResult.FailureHttp(apiResult.errorMessage)
                is ApiResult.Failure -> {
                    try {
                        val errorModel = Gson().fromJson(apiResult.errorJson, ApiResponseError::class.java)
                        DataResult.Failure(errorModel)
                    } catch (e: Exception) {
                        DataResult.FailureHttp("Cannot convert error: ${apiResult.errorJson}")
                    }
                }
                is ApiResult.Success -> {
                    try {
                        DataResult.Success(Gson().fromJson(apiResult.dataJson, T::class.java))
                    } catch (e: Exception) {
                        try {
                            DataResult.Success(Gson().fromJson("{ \"result\" : ${apiResult.dataJson} }", T::class.java))
                        } catch (ex: Exception) {
                            DataResult.FailureHttp("Cannot convert: ${apiResult.dataJson}")
                        }
                    }
                }
            }
            completionHandler(result)
        }
    }
}