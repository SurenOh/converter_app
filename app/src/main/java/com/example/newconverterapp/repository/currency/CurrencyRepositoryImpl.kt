package com.example.newconverterapp.repository.currency

import com.example.newconverterapp.api.DataResult
import com.example.newconverterapp.mapper.currency.CurrencyMapper
import com.example.newconverterapp.network.currency.CurrencyDataFetcher

typealias GetCurrencyDataCallback = (CurrencyStatus) -> Unit

class CurrencyRepositoryImpl(
    private val dataFetcher: CurrencyDataFetcher,
    private val currencyMapper: CurrencyMapper
) : CurrencyRepository {

    override fun getCurrency(completionHandler: GetCurrencyDataCallback) {
        dataFetcher.getCurrencyData { result ->
            val currencyGet = when (result) {
                is DataResult.FailureHttp -> CurrencyStatus.StatusErrorHttp(result.error)
                is DataResult.Failure -> CurrencyStatus.StatusError(result.data.message)
                is DataResult.Success -> CurrencyStatus.StatusSuccess(currencyMapper.mapToModel(result.data))
            }
            completionHandler(currencyGet)
        }
    }

}