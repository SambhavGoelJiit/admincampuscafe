package com.example.admincampuscafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.admincampuscafe.adapter.PendingOrderAdapter
import com.example.admincampuscafe.databinding.ActivityPendingOrderBinding
import com.example.admincampuscafe.model.AllMenu
import com.example.campuscafe.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrderActivity : AppCompatActivity(), PendingOrderAdapter.OnItemClicked {
    private lateinit var binding: ActivityPendingOrderBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var adapter: PendingOrderAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val menuMap: MutableMap<String, AllMenu> = mutableMapOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        swipeRefreshLayout = binding.swipeRefreshLayoutPending

        databaseReference = FirebaseDatabase.getInstance().reference.child("OrderDetails")

        setupRecyclerView()
        getOrderDetails()

        swipeRefreshLayout.setOnRefreshListener {
            getOrderDetails()
        }

        fetchMenuInformationFromDatabase()

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        binding.pendingRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PendingOrderAdapter(this, mutableListOf(), object : PendingOrderAdapter.OnItemClicked {
            override fun onItemClicked(position: Int, orderDetails: OrderDetails) {
                val intent = Intent(this@PendingOrderActivity, OrderDetailsActivity::class.java)
                intent.putExtra("UserOrderDetails", orderDetails)
                startActivity(intent)
            }

            override fun onItemAcceptClicked(position: Int, orderDetails: OrderDetails) {
                updateOrderAcceptStatus(orderDetails)
            }

            override fun onItemDispatchClicked(position: Int, orderDetails: OrderDetails) {
                updateOrderDispatchStatus(orderDetails)
            }
        })
        binding.pendingRecyclerView.adapter = adapter
    }

    private fun getOrderDetails() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listOfOrderItem: MutableList<OrderDetails> = mutableListOf()

                for (orderSnapshot in snapshot.children) {
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        if (!it.orderDispatched) {
                            listOfOrderItem.add(it)
                        }
                    }
                }
                adapter.updateData(listOfOrderItem)
                swipeRefreshLayout.isRefreshing = false
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun fetchMenuInformationFromDatabase() {
        val menuReference = FirebaseDatabase.getInstance().reference.child("menu")
        menuReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (menuSnapshot in snapshot.children) {
                    val menuItem = menuSnapshot.getValue(AllMenu::class.java)
                    menuItem?.foodName?.let {
                        menuMap[it] = menuItem
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun calculatePreparationTime(orderDetails: OrderDetails): Long {
        var maxTime = 0L

        orderDetails.foodNames?.forEach { itemName ->
            val menuInfo = menuMap[itemName]

            val consecutiveCount = menuInfo?.foodConsecutiveCount?.toLong() ?: 1L
            val timePerBatch = 60000 * (menuInfo?.foodTime?.toLong() ?: 0L)
            val quantityOrdered = orderDetails.foodQuantities?.getOrNull(orderDetails.foodNames?.indexOf(itemName)!!) ?: 0

            val totalTimeForItem = ((quantityOrdered / consecutiveCount) + 1) * timePerBatch
            Log.d("prep time", totalTimeForItem.toString())

            if (totalTimeForItem > maxTime) {
                maxTime = totalTimeForItem
            }
        }
        Log.d("prep time", maxTime.toString())
        return maxTime
    }

    private fun calculateReadyTime(orderDetails: OrderDetails): Long {
        val preparationTime = calculatePreparationTime(orderDetails)
        Log.d("prep time", preparationTime.toString())
        val currentTime = System.currentTimeMillis()
        return currentTime + preparationTime
    }

    private fun updateOrderAcceptStatus(orderDetails: OrderDetails) {
        val pushKey = orderDetails.itemPushKey
        databaseReference.child(pushKey!!).child("orderAccepted").setValue(true)
        databaseReference.child(pushKey).child("currentTime").setValue(calculateReadyTime(orderDetails))
    }

    private fun updateOrderDispatchStatus(orderDetails: OrderDetails) {
        val pushKey = orderDetails.itemPushKey
        databaseReference.child(pushKey!!).child("orderDispatched").setValue(true)
    }

    override fun onItemClicked(position: Int, orderDetails: OrderDetails) {
        val intent = Intent(this@PendingOrderActivity, OrderDetailsActivity::class.java)
        intent.putExtra("UserOrderDetails", orderDetails)
        startActivity(intent)
    }

    override fun onItemAcceptClicked(position: Int, orderDetails: OrderDetails) {
        updateOrderAcceptStatus(orderDetails)
    }

    override fun onItemDispatchClicked(position: Int, orderDetails: OrderDetails) {
        updateOrderDispatchStatus(orderDetails)
    }
}