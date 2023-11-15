package com.example.admincampuscafe.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admincampuscafe.databinding.StatusItemBinding

class StatusAdapter(private val customerNames: MutableList<String>, private val statusOrder: MutableList<Boolean>): RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding = StatusItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size

    inner class StatusViewHolder(private val binding: StatusItemBinding):RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNames[position]
                if(statusOrder[position]){
                    orderStatus.text = "Received"
                }else{
                    orderStatus.text == "Not Received"
                }
                val colorMap = mapOf(
                    true to Color.GREEN,
                    false to Color.RED
                )
                orderStatus.setTextColor(colorMap[statusOrder[position]]?:Color.BLACK)
            }
        }

    }
}