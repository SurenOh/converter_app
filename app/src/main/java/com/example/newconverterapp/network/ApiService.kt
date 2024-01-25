package com.example.newconverterapp.network

import com.example.newconverterapp.api.API
import com.example.newconverterapp.api.ApiRequestType
import com.example.newconverterapp.api.ApiResult
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

typealias ApiResponse = (ApiResult) -> Unit

class ApiService(private val client: OkHttpClient) {

    fun procedure(
        api: API,
        completionHandler: ApiResponse
    ) {
        val request = api.apiValue

        when (request.type) {
            ApiRequestType.Get -> get(request.path, generateCallback(completionHandler))
            else -> Unit
        }
    }

    private fun get(url: String, callback: Callback) : Call {
        val request = Request.Builder()
            .url(url)
            .get()

        val call = client.newCall(request.build())
        call.enqueue(callback)

        return call
    }

    private fun generateCallback(completionHandler: ApiResponse): Callback {
        return object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: ""
                if (response.code in 200..299) {
                    completionHandler(ApiResult.Success(body))
                } else {
                    completionHandler(ApiResult.Failure(body))
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                completionHandler(ApiResult.FailureHttp(e.message ?: "Failure Http"))
            }

        }
    }
}