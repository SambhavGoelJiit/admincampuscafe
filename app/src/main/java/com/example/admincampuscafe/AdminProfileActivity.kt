package com.example.admincampuscafe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.admincampuscafe.databinding.ActivityAdminProfileBinding

class AdminProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy{
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.profileName.isEnabled = false
        binding.profileEmail.isEnabled = false
        binding.profilePhone.isEnabled = false
        binding.profileAddress.isEnabled = false
        binding.profilePassword.isEnabled = false

        var isEnable = false

        binding.editButton.setOnClickListener {
            isEnable = !isEnable
            binding.profileName.isEnabled = isEnable
            binding.profileEmail.isEnabled = isEnable
            binding.profilePhone.isEnabled = isEnable
            binding.profileAddress.isEnabled = isEnable
            binding.profilePassword.isEnabled = isEnable
            if (isEnable){
                binding.profileName.requestFocus()
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}