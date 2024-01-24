package com.example.newconverterapp.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newconverterapp.R
import com.example.newconverterapp.databinding.FragmentHomeBinding
import com.example.newconverterapp.model.BalanceModel
import com.example.newconverterapp.model.ConversionItem
import com.example.newconverterapp.model.ConversionModel
import com.example.newconverterapp.model.ErrorMessage
import com.example.newconverterapp.model.Rate
import com.example.newconverterapp.ui.home.adapter.BalanceAdapter
import com.example.newconverterapp.ui.home.dialog.ResultDialog
import com.example.newconverterapp.ui.home.dialog.SelectorDialog
import com.example.newconverterapp.util.getString
import com.example.newconverterapp.util.isInternetAvailable
import com.example.newconverterapp.util.watcher.MyTextWatcher
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModel()

    private var sellItems = listOf<Rate>()
    private var receiveItems = listOf<Rate>()

    private lateinit var sharedPreferences: SharedPreferences
    private val balanceAdapter = BalanceAdapter(arrayListOf())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (_binding == null) _binding = FragmentHomeBinding.inflate(layoutInflater)

        sharedPreferences = requireContext().getSharedPreferences(myShared, Context.MODE_PRIVATE)
        if (getFreeConversions() == errorFreeConversion) changeFreeConversions(defaultFreeConversions)
        setupButtons()
        setupRecycler()
        viewModel.getBalance()
        viewModel.observeCurrency()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        binding.etSellValue.addTextChangedListener(MyTextWatcher(::calculateReceive))
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeViewModel.UiState.OnError -> showResultDialog(
                    R.string.label_error.getString(requireContext()),
                    state.error.message
                )

                is HomeViewModel.UiState.OnGetBalance -> updateBalance(state.balance)
                is HomeViewModel.UiState.OnCalculateConversion -> binding.tvReceiveValue.text =
                    state.receiveValue.toString()

                is HomeViewModel.UiState.OnSuccessConvert -> successConvert(state.data, state.commission)
                is HomeViewModel.UiState.OnGetCurrency -> {
                    receiveItems = state.model.rates
                    sellItems = state.balanceRates
                }
            }
        }
    }

    private fun setupButtons() = with(binding) {
        btnSubmit.setOnClickListener {
            etSellValue.clearFocus()
            if (requireContext().isInternetAvailable()) convert() else showResultDialog(
                R.string.label_error.getString(
                    requireContext()
                ), ErrorMessage.NoInternet.message
            )
        }
        tvSellCurrency.setOnClickListener {
            showCurrencySelectorDialog(R.string.sell_title.getString(requireContext()), sellItems) { code ->
                tvSellCurrency.text = code
                calculateReceive()
            }
        }
        tvReceiveCurrency.setOnClickListener {
            showCurrencySelectorDialog(R.string.receive_title.getString(requireContext()), receiveItems) { code ->
                tvReceiveCurrency.text = code
                calculateReceive()
            }
        }
    }

    private fun convert() = with(binding) {
        viewModel.convert(
            getFreeConversions(),
            etSellValue.text.toString(),
            tvSellCurrency.text.toString(),
            tvReceiveValue.text.toString(),
            tvReceiveCurrency.text.toString(),
        )
    }

    private fun successConvert(data: ConversionModel, commission: ConversionItem) {
        if (getFreeConversions() > 0) changeFreeConversions()
        showResultDialog(
            R.string.successful_conversion_title.getString(requireContext()),
            String.format(
                R.string.successful_description.getString(requireContext()),
                data.sell.value,
                data.sell.code,
                data.receive.value,
                data.receive.code,
                commission.value,
                commission.code
            )
        )
    }

    private fun changeFreeConversions(value: Int? = null) {
        val newValue = value ?: (getFreeConversions() - 1)
        sharedPreferences.edit().putInt(freeConversions, newValue).apply()
    }

    private fun showResultDialog(title: String, description: String) {
        val dialog = ResultDialog(title, description)
        dialog.show(parentFragmentManager, tag)
    }

    private fun showCurrencySelectorDialog(title: String, items: List<Rate>, onGetCurrency: (String) -> Unit) {
        val newItems = items.map { it.code }
        val dialog = SelectorDialog(title, newItems) { onGetCurrency(it) }
        dialog.show(parentFragmentManager, tag)
    }

    private fun calculateReceive() = with(binding) {
        viewModel.calculateReceive(
            getFreeConversions(),
            etSellValue.text.toString(),
            tvSellCurrency.text.toString(),
            tvReceiveCurrency.text.toString()
        )
    }

    private fun updateBalance(balance: List<BalanceModel>) {
        balanceAdapter.setItems(balance)
        binding.tvSellCurrency.text = balance.first().code
    }


    private fun setupRecycler() {
        binding.rvBalances.apply {
            adapter = this@HomeFragment.balanceAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun getFreeConversions() = sharedPreferences.getInt(freeConversions, errorFreeConversion)

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val freeConversions = "freeConversions"
        const val errorFreeConversion = -1
        const val myShared = "my_shared"
        const val defaultFreeConversions = 5
    }
}