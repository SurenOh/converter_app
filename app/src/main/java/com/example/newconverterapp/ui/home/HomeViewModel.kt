package com.example.newconverterapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newconverterapp.model.BalanceModel
import com.example.newconverterapp.model.ConversionItem
import com.example.newconverterapp.model.ConversionModel
import com.example.newconverterapp.model.CurrencyModel
import com.example.newconverterapp.model.ErrorMessage
import com.example.newconverterapp.model.Rate
import com.example.newconverterapp.repository.balance.BalanceRepository
import com.example.newconverterapp.repository.currency.CurrencyRepository
import com.example.newconverterapp.repository.currency.CurrencyStatus
import com.example.newconverterapp.util.roundTwoDigits
import com.example.newconverterapp.util.toDoubleRound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(
    private val balanceRepository: BalanceRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    sealed class UiState {
        class OnGetCurrency(val model: CurrencyModel, val balanceRates: List<Rate>) : UiState()
        class OnGetBalance(val balance: List<BalanceModel>) : UiState()
        class OnSuccessConvert(val data: ConversionModel, val commission: ConversionItem) : UiState()
        class OnCalculateConversion(val receiveValue: Double) : UiState()
        class OnError(val error: ErrorMessage) : UiState()
    }

    private var baseCurrencyValue: String = baseCurrency
    private var currencies: List<Rate> = listOf()
    private var balances: List<BalanceModel> = listOf()

    fun getBalance() {
        viewModelScope.launch(Dispatchers.IO) {
            balances = balanceRepository.getBalance()
           _uiState.postValue(UiState.OnGetBalance(balances))
        }
    }

    fun convert(freeCount: Int, sellValue: String, sellCode: String, receiveValue: String, receiveCode: String) {
        val isEmpty = sellValue.isEmpty() || sellCode.isEmpty() || receiveValue.isEmpty() || receiveCode.isEmpty()
        val sellBalance = balances.find { it.code == sellCode }?.balance ?: zeroBalance

        val error = when {
            isEmpty -> ErrorMessage.FillFields
            sellCode == receiveCode -> ErrorMessage.SameCurrencies
            sellBalance <= zeroBalance || sellBalance < sellValue.toDoubleRound() -> ErrorMessage.NotEnoughMoney
            else -> null
        }

        error?.let {
            _uiState.postValue(UiState.OnError(it))
        } ?: run {
            val sellValueDouble = sellValue.toDoubleRound()
            val sellValueAfterCommission = if (freeCount <= 0) getCommission(sellValueDouble) else sellValueDouble

            val sellItem = ConversionItem(sellValueDouble, sellCode)
            val receiveItem = ConversionItem(receiveValue.toDoubleRound(), receiveCode)

            updateBalances(receiveItem, sellItem)

            val commission = (sellValueDouble - sellValueAfterCommission).roundTwoDigits()

            _uiState.postValue(
                UiState.OnSuccessConvert(
                    ConversionModel(sellItem, receiveItem),
                    ConversionItem(commission, sellItem.code)
                )
            )
        }
    }

    fun calculateReceive(freeCount: Int, sellValue: String, sellCode: String, receiveCode: String) {
        if (sellValue.isEmpty() || sellCode.isEmpty() || receiveCode.isEmpty()) return
        val calculateState = try {
            val sellValueDouble = sellValue.toDoubleRound()

            val sellValueAfterCommission = if (freeCount <= 0) getCommission(sellValueDouble) else sellValueDouble

            val receiveRate = currencies.find { it.code == receiveCode }?.currency ?: errorResultDouble
            val sellRate = currencies.find { it.code == sellCode }?.currency ?: errorResultDouble

            val result = when {
                sellCode == baseCurrencyValue -> sellValueAfterCommission * receiveRate
                receiveCode == baseCurrencyValue -> sellValueAfterCommission / sellRate
                else -> sellValueAfterCommission / sellRate * receiveRate
            }
            if (receiveRate <= 0.0 || sellRate <= 0.0) UiState.OnError(ErrorMessage.IncorrectValue) else UiState.OnCalculateConversion(result.roundTwoDigits())
        } catch (e: Exception) {
            UiState.OnError(ErrorMessage.IncorrectValue)
        }

        _uiState.postValue(calculateState)
    }

    fun observeCurrency() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                getCurrency()
                delay(5000)
            }
        }
    }

    private fun getCommission(value: Double) = (value - value * commission).roundTwoDigits()

    private fun updateBalances(receiveItem: ConversionItem, sellItem: ConversionItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val balanceSellOld = balanceRepository.getSelectedBalance(sellItem.code)
            val balanceReceiveOld = balanceRepository.getSelectedBalance(receiveItem.code)

            balanceSellOld?.let { oldSellBalance ->
                val newSellBalance = oldSellBalance.balance - sellItem.value
                balanceRepository.insertBalance(BalanceModel(newSellBalance, sellItem.code))
            } ?: _uiState.postValue(UiState.OnError(ErrorMessage.Unknown))

            balanceReceiveOld?.let { oldReceiveBalance ->
                val newReceiveBalance = oldReceiveBalance.balance + receiveItem.value
                balanceRepository.insertBalance(BalanceModel(newReceiveBalance, receiveItem.code))
            } ?: balanceRepository.insertBalance(BalanceModel(receiveItem.value, receiveItem.code))

            getBalance()
        }
    }

    private fun getCurrency() {
        currencyRepository.getCurrency { status ->
            when (status) {
                is CurrencyStatus.StatusError -> _uiState.postValue(UiState.OnError(ErrorMessage.ServerError))
                is CurrencyStatus.StatusErrorHttp -> {}
                is CurrencyStatus.StatusSuccess -> {
                    val model = status.model
                    model.base?.let { baseCurrencyValue = it }
                    currencies = model.rates
                    val filteredRates = getFilteredRates(model.rates)
                    _uiState.postValue(UiState.OnGetCurrency(model, filteredRates))
                }
            }

        }
    }

    private fun getFilteredRates(allRates: List<Rate>) = balances.flatMap { element ->
        allRates.filter { it.code == element.code }
    }

    companion object {
        const val zeroBalance = 0.001
        const val errorResultDouble = -1.0
        const val commission = 0.007
        const val baseCurrency = "EUR"
    }
}