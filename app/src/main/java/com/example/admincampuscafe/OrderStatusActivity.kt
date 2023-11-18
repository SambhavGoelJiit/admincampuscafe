package com.example.admincampuscafe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincampuscafe.adapter.StatusAdapter
import com.example.admincampuscafe.databinding.ActivityOrderStatusBinding
import com.example.campuscafe.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class OrderStatusActivity : AppCompatActivity() {
    private val binding: ActivityOrderStatusBinding by lazy {
        ActivityOrderStatusBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var listOfCompleteOrderList: ArrayList<OrderDetails> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        swipeRefreshLayout = binding.swipeRefreshLayoutStatus

        binding.backButton.setOnClickListener {
            finish()
        }

        swipeRefreshLayout.setOnRefreshListener {
            retrieveCompleteOrderDetails()
        }

        retrieveCompleteOrderDetails()
    }

    private fun retrieveCompleteOrderDetails() {
        databaseReference = FirebaseDatabase.getInstance().reference.child("OrderDetails")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfCompleteOrderList.clear()
                for (orderSnapshot in snapshot.children) {
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        if (it.orderDispatched) {
                            listOfCompleteOrderList.add(it)
                        }
                    }
                }
                listOfCompleteOrderList.reverse()
                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setDataIntoRecyclerView() {
        val custName = mutableListOf<String>()
        val totals = mutableListOf<String>()
        for(order in listOfCompleteOrderList){
            order.username?.let { custName.add(it) }
            order.total?.let { totals.add(it) }
        }
        val adapter = StatusAdapter(custName, totals)
        binding.statusRecyclerView.adapter = adapter
        binding.statusRecyclerView.layoutManager = LinearLayoutManager(this)
        swipeRefreshLayout.isRefreshing = false
    }
}