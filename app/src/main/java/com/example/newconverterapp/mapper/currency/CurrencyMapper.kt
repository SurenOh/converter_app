package com.example.newconverterapp.mapper.currency

import com.example.newconverterapp.apimodels.response.CurrencyResponse
import com.example.newconverterapp.model.CurrencyModel
import com.example.newconverterapp.model.Rate
import com.example.newconverterapp.util.Mapper

class CurrencyMapper : Mapper<CurrencyResponse, CurrencyModel> {

    override fun mapToModel(t: CurrencyResponse): CurrencyModel {

        val rates = t.rates?.mapNotNull { dto ->
            dto.key?.let { codeNonNull ->
                dto.value?.let { rateNonNull ->
                    Rate(codeNonNull, rateNonNull)
                }
            }
        } ?: arrayListOf()

        return CurrencyModel(t.base, rates)
    }

}