package com.example.admincampuscafe.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.admincampuscafe.databinding.ItemItemBinding
import com.example.admincampuscafe.model.AllMenu
import com.google.firebase.database.DatabaseReference

class MenuItemAdapter(
    private val context: Context,
    private val menuItem: ArrayList<AllMenu>,
    databaseReference: DatabaseReference
) : RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>() {
    private val itemQuantities = IntArray(menuItem.size) { 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItem.size

    inner class AddItemViewHolder(private val binding: ItemItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                val menuItem = menuItem[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)
                foodNameTextView.text = menuItem.foodName
                priceTextView.text = menuItem.foodPrice
                Glide.with(context).load(uri).into(foodImageView)
                cartItemQuantity.text = quantity.toString()
                minusButton.setOnClickListener {
                    decreaseQty(position)
                }
                plusButton.setOnClickListener {
                    increaseQty(position)
                }
                deleteButton.setOnClickListener {
                    deleteQty(position)
                }
            }
        }

        private fun deleteQty(position: Int) {
            menuItem.removeAt(position)
            menuItem.removeAt(position)
            menuItem.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuItem.size)
        }

        private fun increaseQty(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun decreaseQty(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            }
        }
    }
}