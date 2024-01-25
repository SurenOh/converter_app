package com.example.newconverterapp.repository.balance

import com.example.newconverterapp.model.BalanceModel

interface BalanceRepository {
    suspend fun insertBalance(model: BalanceModel)
    suspend fun removeBalance(code: String)
    suspend fun getBalance(): List<BalanceModel>
    suspend fun getSelectedBalance(code: String): BalanceModel?
}