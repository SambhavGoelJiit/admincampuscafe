package com.example.admincampuscafe.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.admincampuscafe.databinding.ItemItemBinding
import com.example.admincampuscafe.model.AllMenu

class MenuItemAdapter(
    private val context: Context,
    private val menuItem: ArrayList<AllMenu>,
    private val onDeleteClickListener: (position: Int) -> Unit
) : RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>() {

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
            val currentItem = menuItem[position]
            val uri = Uri.parse(currentItem.foodImage)
            with(binding) {
                foodNameTextView.text = currentItem.foodName
                priceTextView.text = currentItem.foodPrice
                Glide.with(context).load(uri).into(foodImageView)
                deleteButton.setOnClickListener {
                    onDeleteClickListener.invoke(adapterPosition)
                }
            }
        }
    }
}
