package com.example.newconverterapp.repository.balance

import com.example.newconverterapp.db.MyDatabase
import com.example.newconverterapp.mapper.balance.BalanceMapper
import com.example.newconverterapp.model.BalanceModel
import com.example.newconverterapp.ui.home.HomeViewModel
import com.example.newconverterapp.ui.home.HomeViewModel.Companion.zeroBalance

class BalanceRepositoryImpl(db: MyDatabase, private val mapper: BalanceMapper) : BalanceRepository {
    private val balanceDao = db.getBalanceDao()

    override suspend fun insertBalance(model: BalanceModel) {
        balanceDao.insertBalance(mapper.mapFromModel(model))
        val balances = balanceDao.getBalance()
        if (balances.isNotEmpty()) {
            balances.forEach { if (it.balance <= zeroBalance) removeBalance(it.code)}
        }
    }

    override suspend fun removeBalance(code: String) {
        balanceDao.removeBalance(code)
    }

    override suspend fun getBalance(): List<BalanceModel> {
        val balances = mapper.mapToModel(balanceDao.getBalance()).ifEmpty {
            val defaultBalanceModel = BalanceModel(defaultBalance, HomeViewModel.baseCurrency)
            insertBalance(defaultBalanceModel)
            listOf(defaultBalanceModel)
        }
        return balances
    }

    override suspend fun getSelectedBalance(code: String) =
        balanceDao.getSelectedBalance(code)?.let { mapper.mapToModel(it) }

    companion object {
        const val defaultBalance = 1000.0
    }
}