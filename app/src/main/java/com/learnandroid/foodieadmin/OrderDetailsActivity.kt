package com.learnandroid.foodieadmin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnandroid.foodieadmin.adapter.OrderDetailsAdapter
import com.learnandroid.foodieadmin.databinding.ActivityOrderDetailsBinding
import com.learnandroid.foodieadmin.model.OrderDetails

class OrderDetailsActivity : AppCompatActivity() {
    private val binding: ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }
    private var userName: String? = null
    private var address: String? = null
    private var phoneNumber: String? = null
    private var totalPrice: String? = null
    private var foodNames: ArrayList<String> = arrayListOf()
    private var foodImages: ArrayList<String> = arrayListOf()
    private var foodQuantity: ArrayList<Int> = arrayListOf()
    private var foodPrices: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(binding.root)

        binding.btnBackOrderDetails.setOnClickListener {
            finish()
        }

        getDataFromIntent()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getDataFromIntent() {
        val receivedOrderDetails = intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        receivedOrderDetails.let { orderDetails ->

            userName = receivedOrderDetails.userName
            foodNames = receivedOrderDetails.foodNames as ArrayList<String>
            foodImages = receivedOrderDetails.foodImages as ArrayList<String>
            foodQuantity = receivedOrderDetails.foodQuantities as ArrayList<Int>
            address = receivedOrderDetails.address
            phoneNumber = receivedOrderDetails.phoneNumber
            foodPrices = receivedOrderDetails.foodPrices as ArrayList<String>
            totalPrice = if (receivedOrderDetails.totalPrice != null)
                receivedOrderDetails.totalPrice else calculateTotalPrice()

            setUserDetails()
            setAdapter()
        }
    }

    private fun calculateTotalPrice(): String {
        var price = 0
        foodPrices.forEach { price += it.toInt() }
        return price.toString()
    }

    private fun setUserDetails() {
        binding.etNameOrderDetails.text = userName
        binding.etAddressOrderDetails.text = address
        binding.etPhoneOrderDetails.text = phoneNumber
        binding.txtTotalAmountOrderDetails.text = totalPrice ?: ""
    }

    private fun setAdapter() {
        binding.orderDetailsRecyclerview.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailsAdapter(this, foodNames, foodImages, foodQuantity, foodPrices)
        binding.orderDetailsRecyclerview.adapter = adapter
    }
}