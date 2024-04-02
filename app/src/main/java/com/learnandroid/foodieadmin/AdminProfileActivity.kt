package com.learnandroid.foodieadmin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.learnandroid.foodieadmin.databinding.ActivityAdminProfileBinding

class AdminProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminReference = database.reference.child("user")

        enableEdgeToEdge()

        setContentView(binding.root)
        setupViews()
        retrieveUserData()
    }

    private fun setupViews() {
        binding.btnBackAdminProfile.setOnClickListener {
            finish()
        }
        binding.btnSaveAdminProfile.setOnClickListener {
            updateUserData()
        }
        binding.etNameAdminProfile.isEnabled = false
        binding.etAddressAdminProfile.isEnabled = false
        binding.etEmailAdminProfile.isEnabled = false
        binding.etPhoneAdminProfile.isEnabled = false
        binding.etPasswordAdminProfile.isEnabled = false
        binding.btnSaveAdminProfile.isEnabled = false

        var isEnable = false
        binding.btnEditYourProfile.setOnClickListener {
            isEnable = !isEnable
            binding.etNameAdminProfile.isEnabled = isEnable
            binding.etAddressAdminProfile.isEnabled = isEnable
            binding.etEmailAdminProfile.isEnabled = isEnable
            binding.etPhoneAdminProfile.isEnabled = isEnable
            binding.etPasswordAdminProfile.isEnabled = isEnable
            if (isEnable) {
                binding.etNameAdminProfile.requestFocus()
            }
            binding.btnSaveAdminProfile.isEnabled = isEnable
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun retrieveUserData() {
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            val userReference = adminReference.child(currentUserUid)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val ownerName = snapshot.child("name").getValue(String::class.java)
                        val email = snapshot.child("email").getValue(String::class.java)
                        val password = snapshot.child("password").getValue(String::class.java)
                        val address = snapshot.child("address").getValue(String::class.java)
                        val phoneNumber = snapshot.child("phone").getValue(String::class.java)

                        setDataToTextView(ownerName, email, password, address, phoneNumber)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled event
                }
            })
        }
    }

    private fun setDataToTextView(
        ownerName: String?,
        email: String?,
        password: String?,
        address: String?,
        phoneNumber: String?
    ) {
        binding.etNameAdminProfile.setText(ownerName)
        binding.etEmailAdminProfile.setText(email)
        binding.etPasswordAdminProfile.setText(password)
        binding.etAddressAdminProfile.setText(address)
        binding.etPhoneAdminProfile.setText(phoneNumber)
    }

    private fun updateUserData() {
        val updateName = binding.etNameAdminProfile.text.toString()
        val updateEmail = binding.etEmailAdminProfile.text.toString()
        val updatePassword = binding.etPasswordAdminProfile.text.toString()
        val updateAddress = binding.etAddressAdminProfile.text.toString()
        val updatePhoneNumber = binding.etPhoneAdminProfile.text.toString()
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            val userReference = adminReference.child(currentUserUid)
            userReference.child("name").setValue(updateName)
            userReference.child("email").setValue(updateEmail)
            userReference.child("password").setValue(updatePassword)
            userReference.child("phone").setValue(updatePhoneNumber)
            userReference.child("address").setValue(updateAddress)

            Toast.makeText(this, "Profile Updated Successfully😊", Toast.LENGTH_SHORT).show()

            // Update email and password for Firebase Authentication
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)
        } else {
            Toast.makeText(this, "Profile Update Failed☹️", Toast.LENGTH_SHORT).show()
        }
    }
}
