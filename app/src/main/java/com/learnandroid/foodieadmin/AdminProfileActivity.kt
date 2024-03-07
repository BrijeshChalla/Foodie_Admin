package com.learnandroid.foodieadmin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.learnandroid.foodieadmin.databinding.ActivityAdminProfileBinding

class AdminProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(binding.root)
        binding.btnBackAdminProfile.setOnClickListener {
            finish()
        }
        binding.etNameAdminProfile.isEnabled = false
        binding.etAddressAdminProfile.isEnabled = false
        binding.etEmailAdminProfile.isEnabled = false
        binding.etPhoneAdminProfile.isEnabled = false
        binding.etPasswordAdminProfile.isEnabled = false

        var isEnable = false
        binding.btnEditYourProfile.setOnClickListener {
            isEnable = ! isEnable
            binding.etNameAdminProfile.isEnabled = isEnable
            binding.etAddressAdminProfile.isEnabled = isEnable
            binding.etEmailAdminProfile.isEnabled = isEnable
            binding.etPhoneAdminProfile.isEnabled = isEnable
            binding.etPasswordAdminProfile.isEnabled = isEnable
            if (isEnable){
                binding.etNameAdminProfile.requestFocus()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
}