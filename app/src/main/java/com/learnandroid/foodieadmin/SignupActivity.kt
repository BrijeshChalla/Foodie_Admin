package com.learnandroid.foodieadmin

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.learnandroid.foodieadmin.databinding.ActivitySignupBinding
import com.learnandroid.foodieadmin.model.UserModel

class SignupActivity : AppCompatActivity() {

    private lateinit var userName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var nameOfRestaurant: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //initialize Firebase Auth
        auth = Firebase.auth
        //initialize Firebase database
        database = Firebase.database.reference

        val locationList = arrayOf("Gujarat", "Maharashtra", "Rajasthan", "Goa")
        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.tvLocationList
        autoCompleteTextView.setAdapter(adapter)

        //Remove below code this is for testing
        binding.btnSignup.setOnClickListener {

            //get text from edittext
            userName = binding.etOwnerNameSignup.text.toString().trim()
            nameOfRestaurant = binding.etRestaurantNameSignup.text.toString().trim()
            email = binding.etEmailSignup.text.toString().trim()
            password = binding.etPasswordSignup.text.toString().trim()

            if (userName.isBlank() || nameOfRestaurant.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }
        }

        binding.btnAlreadyHave.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure", task.exception)
            }
        }
    }

    //save data into database
    private fun saveUserData() {
        // save data in database
        userName = binding.etOwnerNameSignup.text.toString().trim()
        nameOfRestaurant = binding.etRestaurantNameSignup.text.toString().trim()
        email = binding.etEmailSignup.text.toString().trim()
        password = binding.etPasswordSignup.text.toString().trim()
        val user = UserModel(userName, nameOfRestaurant, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        //save user data in firebase database
        database.child("user").child(userId).setValue(user)
    }
}