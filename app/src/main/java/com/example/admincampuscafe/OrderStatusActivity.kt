package com.example.admincampuscafe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincampuscafe.adapter.StatusAdapter
import com.example.admincampuscafe.databinding.ActivityOrderStatusBinding
import com.example.campuscafe.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderStatusActivity : AppCompatActivity() {
    private val binding: ActivityOrderStatusBinding by lazy {
        ActivityOrderStatusBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private var listOfCompleteOrderList: ArrayList<OrderDetails> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }
        retrieveCompleteOrderDetails()
    }

    private fun retrieveCompleteOrderDetails() {
        database = FirebaseDatabase.getInstance()
        val completeOrderRef = database.reference.child("CompletedOrder").orderByChild("currentTime")
        completeOrderRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    listOfCompleteOrderList.clear()
                    val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfCompleteOrderList.add(it)
                    }
                }
                listOfCompleteOrderList.reverse()
                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setDataIntoRecyclerView() {
        val custName = mutableListOf<String>()
        val moneyStatus = mutableListOf<Boolean>()
        for(order in listOfCompleteOrderList){
            order.username?.let { custName.add(it) }
            moneyStatus.add(order.paymentReceived)

        }
        val adapter = StatusAdapter(custName, moneyStatus)
        binding.statusRecyclerView.adapter = adapter
        binding.statusRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}