package com.example.admincampuscafe.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.admincampuscafe.databinding.PendingOrdersItemBinding
import com.example.campuscafe.model.OrderDetails

class PendingOrderAdapter(
    private val context: Context,
    private val orderDetailsList: MutableList<OrderDetails>,
    private val itemClicked: OnItemClicked
) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {

    interface OnItemClicked {
        fun onItemClicked(position: Int, orderDetails: OrderDetails)
        fun onItemAcceptClicked(position: Int, orderDetails: OrderDetails)
        fun onItemDispatchClicked(position: Int, orderDetails: OrderDetails)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(updatedList: List<OrderDetails>) {
        orderDetailsList.clear()
        orderDetailsList.addAll(updatedList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding = PendingOrdersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        holder.bind(orderDetailsList[position])
    }

    override fun getItemCount(): Int = orderDetailsList.size

    inner class PendingOrderViewHolder(private val binding: PendingOrdersItemBinding): RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false
        @SuppressLint("SetTextI18n")
        fun bind(orderDetails: OrderDetails) {
            binding.apply {
                customerName.text = orderDetails.username
                totalAmt.text = orderDetails.total
                orderAcceptButton.apply {
                    text = if (!isAccepted) {
                        "Accept"
                    } else {
                        "Dispatch"
                    }
                    setOnClickListener {
                        if (!isAccepted) {
                            text = "Dispatch"
                            isAccepted = true
                            showToast("Order is Accepted")
                            itemClicked.onItemAcceptClicked(adapterPosition, orderDetails)
                        } else {
                            val currentPosition = adapterPosition
                            orderDetailsList.removeAt(currentPosition)
                            notifyItemRemoved(currentPosition)
                            showToast("Order Is Dispatched")
                            itemClicked.onItemDispatchClicked(currentPosition, orderDetails)
                        }
                    }
                }
                itemView.setOnClickListener {
                    itemClicked.onItemClicked(adapterPosition, orderDetails)
                }
            }
        }
        private fun showToast(message: String){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}