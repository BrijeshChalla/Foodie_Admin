package com.learnandroid.foodieadmin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.learnandroid.foodieadmin.databinding.ActivityMainBinding
import com.learnandroid.foodieadmin.model.OrderDetails

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completedOrderReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.addMenuLayout.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }
        binding.allItemMenuLayout.setOnClickListener {
            startActivity(Intent(this, AllItemActivity::class.java))
        }
        binding.orderDispatchLayout.setOnClickListener {
            startActivity(Intent(this, OutForDeliveryActivity::class.java))
        }
        binding.profileLayout.setOnClickListener {
            startActivity(Intent(this, AdminProfileActivity::class.java))
        }
        binding.createNewUserLayout.setOnClickListener {
            startActivity(Intent(this, CreateUserActivity::class.java))
        }
        binding.txtPendingOrder.setOnClickListener {
            startActivity(Intent(this, PendingOrderActivity::class.java))
        }
        binding.logoutLayout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        pendingOrders()
        completedOrders()
        wholeTimeEarning()
    }

    private fun wholeTimeEarning() {
        var listOfTotalPay = mutableListOf<Int>()
        completedOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    var completeOrder = orderSnapshot.getValue(OrderDetails::class.java)

                    completeOrder?.totalPrice?.replace("₹", "")?.toIntOrNull()
                        ?.let { i -> listOfTotalPay.add(i) }
                }
                binding.txtWholeEarningCount.text = listOfTotalPay.sum().toString() + "₹"
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })
    }

    private fun completedOrders() {
        completedOrderReference = database.reference.child("CompletedOrder")
        completedOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val completedOrderItemCount = snapshot.childrenCount.toInt()
                binding.txtCompletedOrderCount.text = completedOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })
    }

    private fun pendingOrders() {
        val pendingOrderReference = database.reference.child("OrderDetails")
        pendingOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val pendingOrderItemCount = snapshot.childrenCount.toInt()
                binding.txtPendingOrderCount.text = pendingOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })
    }
}
