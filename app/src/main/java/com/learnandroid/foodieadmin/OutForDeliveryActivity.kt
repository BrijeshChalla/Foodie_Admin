package com.learnandroid.foodieadmin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnandroid.foodieadmin.adapter.DeliveryAdapter
import com.learnandroid.foodieadmin.databinding.ActivityOutForDeliveryBinding

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(binding.root)

        val customerName = arrayListOf(
            "John Doe",
            "Jane Smith",
            "Mike Johnson"
        )
        val moneyStatus = arrayListOf(
            "Received",
            "Not Received",
            "Pending"
        )
        val adapter = DeliveryAdapter(customerName, moneyStatus)
        binding.outForDeliverRecyclerView.adapter = adapter
        binding.outForDeliverRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.btnBackOutForDelivery.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
}