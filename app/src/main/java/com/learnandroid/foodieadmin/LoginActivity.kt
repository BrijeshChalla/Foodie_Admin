package com.learnandroid.foodieadmin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.learnandroid.foodieadmin.databinding.ActivityLoginBinding
import com.learnandroid.foodieadmin.model.UserModel

class LoginActivity : AppCompatActivity() {

    private var userName: String? = null
    private var nameOfRestaurant: String? = null
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        //initialize Firebase Auth
        auth = Firebase.auth
        //initialize firebase Database
        database = Firebase.database.reference
        //initialize google sign in
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)

        binding.btnLogin.setOnClickListener {
            //get text from edittext
            email = binding.etEmailLogin.text.toString().trim()
            password = binding.etPasswordLogin.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please Fill All Details", Toast.LENGTH_SHORT).show()
            } else {
                createUserAccount(email, password)
            }
        }
        binding.btnGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
        binding.btnDontHave.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun createUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                updateUi(user)
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(
                            this,
                            "New user Created & Login Successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        saveUserData()
                        updateUi(user)
                    } else {
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                        Log.d("Account", "createUserAccount: Authentication Failed", task.exception)
                    }
                }
            }
        }
    }

    private fun saveUserData() {
        //get text from edittext
        email = binding.etEmailLogin.text.toString().trim()
        password = binding.etPasswordLogin.text.toString().trim()

        val user = UserModel(userName, nameOfRestaurant, email, password)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            database.child("user").child(it).setValue(user)
        }
    }

    //launcher for google sign in
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful){
                val account : GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful){
                        // successfully sign in with google
                        Toast.makeText(this,"Successfully Sign-In With Google",Toast.LENGTH_SHORT).show()
                        updateUi(authTask.result?.user)
                        finish()
                    } else
                    {
                        Toast.makeText(this,"Google Sign-In Failed",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(this,"Google Sign-In Failed",Toast.LENGTH_SHORT).show()
            }
        }
    }
    // check if user is already logged in
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
        }
    }
    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}