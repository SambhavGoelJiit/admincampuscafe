package com.example.admincampuscafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincampuscafe.adapter.PendingOrderAdapter
import com.example.admincampuscafe.databinding.ActivityPendingOrderBinding
import com.example.campuscafe.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrderActivity : AppCompatActivity(), PendingOrderAdapter.onItemClicked {
    private lateinit var binding: ActivityPendingOrderBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private var listOfName: MutableList<String> = mutableListOf()
    private var listOfTotalPrice: MutableList<String> = mutableListOf()
    private var listOfOrderItem: MutableList<OrderDetails> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference.child("OrderDetails")

        getOrderDetails()

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun getOrderDetails() {

        database = FirebaseDatabase.getInstance()
        val orderRef: DatabaseReference = database.reference.child("OrderDetails")
        orderRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let { listOfOrderItem.add(it) }
                }
                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addDataToListForRecyclerView() {
        for (orderItem in listOfOrderItem) {
            orderItem.username?.let { listOfName.add(it) }
            orderItem.total?.let { listOfTotalPrice.add(it) }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.pendingRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = PendingOrderAdapter(this, listOfName, listOfTotalPrice, this)
        binding.pendingRecyclerView.adapter = adapter
    }

    override fun onItemClickedListener(position: Int) {
        val intent = Intent(this, OrderDetailsActivity::class.java)
        val userOrderDetails = listOfOrderItem[position]
        intent.putExtra("UserOrderDetails", userOrderDetails)
        startActivity(intent)
    }

    override fun onItemAcceptClickedListener(position: Int) {
        val childItemPushKey = listOfOrderItem[position].itemPushKey
        val clickItemOrderRef = childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrderRef?.child("orderAccepted")?.setValue(true)
        updateOrderAcceptStatus(position)
    }

    override fun onItemDispatchClickedListener(position: Int) {
        val dispatchItemPushKey = listOfOrderItem[position].itemPushKey
        val dispatchOrderItemRef =
            database.reference.child("CompletedOrder").child(dispatchItemPushKey!!)
        dispatchOrderItemRef.setValue(listOfOrderItem[position]).addOnSuccessListener {
            deleteThisItemFromOrderDetails(dispatchItemPushKey)
        }
    }

    private fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String) {
        val orderDetailsItemRef =
            database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderDetailsItemRef.removeValue().addOnSuccessListener {
            Toast.makeText(this, "Order Is Dispatched", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Unable to Dispatch", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateOrderAcceptStatus(position: Int) {
        val userIdOfClickedItem = listOfOrderItem[position].uid
        val pushKeyOfClickedItem = listOfOrderItem[position].itemPushKey
        val buyHistoryReference =
            database.reference.child("userMainApp").child(userIdOfClickedItem!!).child("PurHistory")
                .child(pushKeyOfClickedItem!!)
        buyHistoryReference.child("orderAccepted").setValue(true)
        databaseReference.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true)
    }
}