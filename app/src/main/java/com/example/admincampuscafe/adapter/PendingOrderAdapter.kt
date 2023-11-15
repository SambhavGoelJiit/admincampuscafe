package com.example.admincampuscafe.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.admincampuscafe.databinding.PendingOrdersItemBinding

class PendingOrderAdapter(
    private val context: Context,
    private val customerNames: MutableList<String>,
    private val total: MutableList<String>,
    private val itemClicked: onItemClicked
): RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {

    interface onItemClicked{
        fun onItemClickedListener(position: Int)
        fun onItemAcceptClickedListener(position: Int)
        fun onItemDispatchClickedListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding = PendingOrdersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size

    inner class PendingOrderViewHolder(private val binding: PendingOrdersItemBinding): RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false
        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNames[position]
                totalAmt.text = total[position]
                orderAcceptButton.apply {
                    if(!isAccepted){
                        text = "Accept"
                    }else{
                        text = "Dispatch"
                    }
                    setOnClickListener{
                        if (!isAccepted){
                            text = "Dispatch"
                            isAccepted = true
                            showToast("Order is Accepted")
                            itemClicked.onItemAcceptClickedListener(position)
                        }else{
                            customerNames.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order Is Dispatched")
                            itemClicked.onItemDispatchClickedListener(position)
                        }
                    }
                }
                itemView.setOnClickListener {
                    itemClicked.onItemClickedListener(position)
                }
            }
        }
        private fun showToast(message: String){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}