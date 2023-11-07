package com.example.admincampuscafe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincampuscafe.adapter.StatusAdapter
import com.example.admincampuscafe.databinding.ActivityOrderStatusBinding

class OrderStatusActivity : AppCompatActivity() {
    private val binding: ActivityOrderStatusBinding by lazy {
        ActivityOrderStatusBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener{
            finish()
        }

        val customerName = arrayListOf(
            "Sambhav", "Sangeet", "Parth"
        )
        val orderStatus = arrayListOf(
            "Ready", "Pending", "Ready"
        )
        val adapter = StatusAdapter(customerName, orderStatus)
        binding.statusRecyclerView.adapter = adapter
        binding.statusRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}