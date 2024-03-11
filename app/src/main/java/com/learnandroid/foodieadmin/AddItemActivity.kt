package com.learnandroid.foodieadmin

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.learnandroid.foodieadmin.databinding.ActivityAddItemBinding
import com.learnandroid.foodieadmin.model.AllMenu

class AddItemActivity : AppCompatActivity() {

    // Food Item Details
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private var foodImageUri: Uri? = null
    private lateinit var foodInfo: String
    private lateinit var foodIngredient: String

    //Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(binding.root)

        // initialize Firebase
        auth = FirebaseAuth.getInstance()
        // initialize Firebase Database instance
        database = FirebaseDatabase.getInstance()

        binding.btnAdditem.setOnClickListener {
            // Get data from Fields
            foodName = binding.etEnterFoodName.text.toString().trim()
            foodPrice = binding.etEnterFoodPrice.text.toString().trim()
            foodInfo = binding.etFoodInfo.text.toString().trim()
            foodIngredient = binding.etFoodIngredients.text.toString().trim()

            if (!(foodName.isBlank() || foodPrice.isBlank() || foodInfo.isBlank() || foodIngredient.isBlank())) {
                uploadData()
                Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Fill all Fields", Toast.LENGTH_SHORT).show()
            }
        }
        binding.tvSelectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.btnBackAddItem.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun uploadData() {
        // get a reference to the "menu" node in the database
        val menuRef = database.getReference("menu")
        // Generate a unique key for the new menu item
        val newItemKey = menuRef.push().key

        if (foodImageUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("menu_images/${newItemKey}.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    // create a new menu item
                    val newItem = AllMenu(
                        foodName = foodName,
                        foodPrice = foodPrice,
                        foodInfo = foodInfo,
                        foodIngredient = foodIngredient,
                        foodImage = downloadUrl.toString()
                    )
                    newItemKey?.let { key ->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this, "Data uploaded Successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                            .addOnFailureListener {
                                Toast.makeText(this, "Data uploading Failed", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                }
            }
                .addOnFailureListener {
                    Toast.makeText(this, "Image uploading Failed", Toast.LENGTH_SHORT).show()
                }
        }
            else {
                Toast.makeText(this, "Please select An Image", Toast.LENGTH_SHORT).show()
            }
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                binding.imgSelectedImage.setImageURI(uri)
                foodImageUri = uri
            }
        }
}