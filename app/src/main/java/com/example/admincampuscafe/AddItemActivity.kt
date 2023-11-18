package com.example.admincampuscafe

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.admincampuscafe.databinding.ActivityAddItemBinding
import com.example.admincampuscafe.model.AllMenu
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class AddItemActivity : AppCompatActivity() {

    private lateinit var foodNameAct: String
    private lateinit var foodPriceAct: String
    private var foodImageActUri: Uri? = null
    private lateinit var foodCountCons: String
    private lateinit var foodTime: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }
        binding.selectedImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.addItemButton.setOnClickListener {
            foodNameAct = binding.enterFoodName.text.toString().trim()
            foodPriceAct = binding.enterFoodPrice.text.toString().trim()
            foodCountCons = binding.enterFoodItemCountConsecutive.text.toString().trim()
            foodTime = binding.enterFoodItemTime.text.toString().trim()

            if (!(foodNameAct.isBlank() || foodPriceAct.isBlank())) {
                uploadData()
                Toast.makeText(this, "Please Wait for data to upload", LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "All Fields Necessary", LENGTH_SHORT).show()
            }
        }
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun uploadData() {
        val menuRef = database.getReference("menu")
        val newItemKey = menuRef.push().key

        val menuQuery = menuRef.orderByChild("foodName").equalTo(foodNameAct)
        menuQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(this@AddItemActivity, "Item already exists", LENGTH_SHORT).show()
                } else {
                    if (foodImageActUri != null) {
                        val storageRef = FirebaseStorage.getInstance().reference
                        val imageRef = storageRef.child("menu_images/${newItemKey}.jpg")
                        val uploadTask = imageRef.putFile(foodImageActUri!!)

                        uploadTask.addOnSuccessListener {
                            imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                val newItem = AllMenu(
                                    newItemKey,
                                    foodName = foodNameAct,
                                    foodPrice = foodPriceAct,
                                    foodImage = downloadUrl.toString(),
                                    foodConsecutiveCount = foodCountCons,
                                    foodTime = foodTime
                                )
                                newItemKey?.let { key ->
                                    menuRef.child(key).setValue(newItem).addOnSuccessListener {
                                        Toast.makeText(this@AddItemActivity, "Data Uploaded", LENGTH_SHORT).show()
                                        startActivity(Intent(this@AddItemActivity, MainActivity::class.java))
                                        finish()
                                    }
                                        .addOnFailureListener {
                                            Toast.makeText(this@AddItemActivity, "Unable to Upload Data", LENGTH_SHORT).show()
                                        }
                                }
                            }
                        }
                            .addOnFailureListener {
                                Toast.makeText(this@AddItemActivity, "Unable to Upload Image", LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this@AddItemActivity, "Please select an Image", LENGTH_SHORT).show()
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }


    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.selectedImage.setImageURI(uri)
            foodImageActUri = uri
        }
    }
}