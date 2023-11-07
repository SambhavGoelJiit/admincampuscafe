package com.example.admincampuscafe.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admincampuscafe.databinding.StatusItemBinding

class StatusAdapter(private val customerNames: ArrayList<String>, private val statusOrder: ArrayList<String>): RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding = StatusItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size

    inner class StatusViewHolder(private val binding: StatusItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNames[position]
                orderStatus.text = statusOrder[position]
                val colorMap = mapOf(
                    "Ready" to Color.GREEN,
                    "Pending" to Color.RED
                )
                orderStatus.setTextColor(colorMap[statusOrder[position]]?:Color.BLACK)
            }
        }

    }
}