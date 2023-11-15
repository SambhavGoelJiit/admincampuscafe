package com.example.admincampuscafe.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admincampuscafe.databinding.OrderdetailsItemBinding

class OrderDetailsAdapter(
    private var context: Context,
    private var foodNames: MutableList<String>,
    private var foodQuantities: MutableList<Int>,
    private var foodPrices: MutableList<String>,
) : RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val binding =
            OrderdetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = foodNames.size

    inner class OrderDetailsViewHolder(private val binding: OrderdetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                foodNameOrDet.text = foodNames[position]
                foodQuantityOrDet.text = foodQuantities[position].toString()
                priceOrDet.text = foodPrices[position]
            }
        }

    }
}