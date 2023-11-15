package com.example.admincampuscafe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincampuscafe.adapter.OrderDetailsAdapter
import com.example.admincampuscafe.databinding.ActivityOrderDetailsBinding
import com.example.campuscafe.model.OrderDetails

class OrderDetailsActivity : AppCompatActivity() {
    private val binding: ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }

    private var userName: String? = null
    private var totalPrice: String? = null
    private var phoneNumber: String? = null
    private var foodNames = mutableListOf<String>()
    private var foodPrices = mutableListOf<String>()
    private var foodQuantities = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener{
            finish()
        }

        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        val receivedOrderDetails = intent.getSerializableExtra("UserOrderDetails") as? OrderDetails
        receivedOrderDetails?.let { orderDetails ->
            userName = orderDetails.username
            totalPrice = orderDetails.total
            phoneNumber = orderDetails.phoneNumber
            foodNames = orderDetails.foodNames!!
            foodPrices = orderDetails.foodPrices!!
            foodQuantities = orderDetails.foodQuantities!!
        }
        setUserDetails()
        setAdapter()
    }

    private fun setAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailsAdapter(this, foodNames, foodQuantities, foodPrices)
        binding.recyclerView.adapter = adapter
    }

    private fun setUserDetails() {
        binding.apply {
            custName.text = userName
            custPhone.text = phoneNumber
            custAmount.text = totalPrice
        }
    }
}