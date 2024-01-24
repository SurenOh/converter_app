package com.example.newconverterapp.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newconverterapp.databinding.ItemBalanceBinding
import com.example.newconverterapp.model.BalanceModel
import com.example.newconverterapp.util.roundTwoDigits

class BalanceAdapter(list: List<BalanceModel>) : RecyclerView.Adapter<BalanceAdapter.ViewHolder>() {
    private lateinit var binding: ItemBalanceBinding
    private var balanceList: ArrayList<BalanceModel>

    init {
        balanceList = ArrayList(list)
    }

    override fun getItemCount() = balanceList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemBalanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(balanceList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newList: List<BalanceModel>) {
        val diffResult = DiffUtil.calculateDiff(BalanceDiffCallback(balanceList, newList))
        balanceList = ArrayList(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private val _binding: ItemBalanceBinding) : RecyclerView.ViewHolder(_binding.root) {
        fun bind(model: BalanceModel) {
            with(_binding) {
                balance.text = model.balance.roundTwoDigits().toString()
                currency.text = model.code
            }
        }
    }

    inner class BalanceDiffCallback(private val oldList: List<BalanceModel>, private val newList: List<BalanceModel>) :
        DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].code == newList[newItemPosition].code

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }

}