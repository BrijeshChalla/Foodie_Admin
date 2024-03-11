package com.learnandroid.foodieadmin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnandroid.foodieadmin.adapter.DeliveryAdapter
import com.learnandroid.foodieadmin.adapter.PendingOrderAdapter
import com.learnandroid.foodieadmin.databinding.ActivityPendingOrderBinding

class PendingOrderActivity : AppCompatActivity() {
    private val binding: ActivityPendingOrderBinding by lazy {
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(binding.root)

        binding.btnBackPendingOrder.setOnClickListener {
            finish()
        }
        val orderedCustomerName = arrayListOf(
            "John Doe",
            "Jane Smith",
            "Mike Johnson"
        )
        val orderedQuantity = arrayListOf(
            "2",
            "5",
            "6"
        )
        val orderedFoodImage = arrayListOf(R.drawable.menu5,R.drawable.menu4,R.drawable.menu3)
        val adapter = PendingOrderAdapter(orderedCustomerName, orderedQuantity, orderedFoodImage, this)
        binding.pendingOrderRecyclerView.adapter = adapter
        binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
}