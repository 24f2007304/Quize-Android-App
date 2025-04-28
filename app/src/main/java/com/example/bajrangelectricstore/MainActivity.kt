package com.example.bajrangelectricstore

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.example.bajrangelectricstore.databinding.MainActivityBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : ComponentActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Initialize the Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // ✅ Correct reference
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)


        // Make clickable link for "New User" text
        click_link_maker.makeClickLink(this, binding.tvNewUser)

        binding.loginbutton.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.userPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }


        binding.google.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
             googleSignInResultLauncher.launch(signInIntent)
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, home_activity::class.java))
            finish()
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    showCustomToast("Login Successful")
                    startActivity(Intent(this, home_activity::class.java))
                    finish()
                } else {
                    val exception = task.exception

                    val errorMessage = exception?.localizedMessage?: "Login Failed: Unknown error occurred"
                    showCustomToast("Error : $errorMessage")

                }
            }
    }
    private fun showCustomToast(message: String) {
        // Inflate the custom layout for the toast
        val inflater = LayoutInflater.from(this)
        val layout = inflater.inflate(R.layout.custome_toast_inflate, null)

        // Set the custom message
        val text: TextView = layout.findViewById(R.id.customToastText)
        text.text = message

        // Create the custom toast
        val customToast = Toast(applicationContext)
        customToast.duration = Toast.LENGTH_SHORT // Set toast duration
        customToast.view = layout // Use the custom layout
        customToast.setGravity(Gravity.TOP, 0, 100)  // Gravity.TOP moves it to the top of the screen, adjust '200' for vertical offset

        // Show the custom toast
        customToast.show()
    }

    // Register for the result of the Google Sign-In
    private val googleSignInResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google Sign-In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign-In failed, handle the error
                Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Firebase authentication with Google Account
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign-in success, update UI
                    Toast.makeText(this, "Google Sign-In Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, home_activity::class.java))
                    finish()
                } else {
                    // If sign-in fails, display a message to the user.
                    Toast.makeText(this, "Google Sign-In failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}