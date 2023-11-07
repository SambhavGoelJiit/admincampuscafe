package com.example.admincampuscafe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincampuscafe.adapter.PendingOrderAdapter
import com.example.admincampuscafe.adapter.StatusAdapter
import com.example.admincampuscafe.databinding.ActivityPendingOrderBinding
import com.example.admincampuscafe.databinding.PendingOrdersItemBinding

class PendingOrderActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPendingOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener{
            finish()
        }

        val orderedCustomerName = arrayListOf(
            "Sambhav", "Sangeet", "Parth"
        )
        val orderedQuantity = arrayListOf(
            "8", "6", "5"
        )
        val orderedFoodImage = arrayListOf(
            R.drawable.menu1,
            R.drawable.menu2,
            R.drawable.menu3
        )
        val adapter = PendingOrderAdapter(orderedCustomerName, orderedQuantity, orderedFoodImage, this)
        binding.pendingRecyclerView.adapter = adapter
        binding.pendingRecyclerView.layoutManager = LinearLayoutManager(this)

    }
}