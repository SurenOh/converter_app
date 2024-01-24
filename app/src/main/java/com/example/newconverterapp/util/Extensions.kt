package com.example.newconverterapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.math.BigDecimal
import java.math.RoundingMode

fun Double.roundTwoDigits(): Double {
    return BigDecimal(this).setScale(2, RoundingMode.HALF_EVEN).toDouble()
}

fun Int.getString(context: Context) = context.resources?.getString(this) ?: ""

fun String.toDoubleRound(): Double {
    val double = this.toDouble()
    return BigDecimal(double).setScale(2, RoundingMode.HALF_EVEN).toDouble()
}

@Suppress("DEPRECATION")
fun Context.isInternetAvailable(): Boolean {
    return try {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        }
        false
    } catch (e: Exception) {
        false
    }
}