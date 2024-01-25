package com.example.newconverterapp.api

import com.example.newconverterapp.util.Constants

enum class ApiRequestType {
    Get, GetWithParams, Post, PostMultipart, Put, Delete
}

class ApiValue(path: String, requestType: ApiRequestType) {
    val path: String
    val type: ApiRequestType = requestType

    init {
        this.path = "${Constants.baseUrl}$path"
    }
}