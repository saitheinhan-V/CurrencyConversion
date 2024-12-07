package com.currency.conversion.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.currency.conversion.databinding.LayoutRvCurrencyItemBinding
import com.currency.conversion.domain.model.Rate
import com.currency.conversion.presentation.utils.decimalSeparator

class RateAdapter(
    private val rateList: MutableList<Rate>,
    private var sourceAmount: Double,
    val clickListener: ItemClickListener
) : RecyclerView.Adapter<RateAdapter.DataViewHolder>() {

    private var onItemClickListener: ((Rate) -> Unit)? = null

    inner class DataViewHolder(private val binding: LayoutRvCurrencyItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Rate) {
            val totalAmount = item.amount * sourceAmount
            binding.tvShortCurrencyName.text = item.key.substring(3,6)
            binding.tvTotalRate.text = decimalSeparator(totalAmount)

            binding.root.apply {
                setOnClickListener {
                    clickListener.onRateItemClick(item)
                }
            }

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(item)
                }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutRvCurrencyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = rateList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val article = rateList[position]
        holder.bind(article)
    }

    fun addData(list: List<Rate>) {
        rateList.clear()
        rateList.addAll(list)
    }

    fun updateData(newRates: List<Rate>) {
        rateList.clear()
        rateList.addAll(newRates)
        notifyDataSetChanged()
    }

    fun updateAmount(amount: Double){
        sourceAmount = amount
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (Rate) -> Unit) {
        onItemClickListener = listener
    }

    interface ItemClickListener {
        fun onRateItemClick(data: Rate)
    }

}