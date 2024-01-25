package com.example.newconverterapp.api

sealed class ApiResult {
    class Success(val dataJson: String) : ApiResult()
    class Failure(val errorJson: String) : ApiResult()
    class FailureHttp(val errorMessage: String) : ApiResult()
}