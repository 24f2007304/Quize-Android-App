package com.example.bajrangelectricstore

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bajrangelectricstore.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignIn_Activity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding =
        ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Intialize FireBase

        auth = FirebaseAuth.getInstance()


        binding.button.setOnClickListener {
            var Email = binding.emailinput.text.toString()
            var Password = binding.password.text.toString()
            var ConfirmPassword = binding.conformPassword.text.toString()

            if (Email.isEmpty() || Password.isEmpty() || ConfirmPassword.isEmpty()) {
                showCostomeToast("Please Fill all the Fields")
            }else if(Password != ConfirmPassword){

                showCostomeToast("Conferm Password Must be Same")
            }else{
                auth.createUserWithEmailAndPassword(Email,Password)
                    .addOnCompleteListener(this) {task ->
                        if(task.isSuccessful){
                            showCostomeToast("Conferm Password Must be Same")
                            auth.signOut()
//                            So the User SignOut Automatically by Creating Account
                            startActivity(Intent(this, MainActivity::class.java))

                            finish()
//                            So when user presses Back Button, they cannot come back to this closed Activity.
//                            After successful registration, the user is already logged into your app. in Firebase authentication, so current user not null  so direct go to the home activity
                        } else{
                            // Account creation failed
                            val exception = task.exception

                            val errorMessage = exception?.localizedMessage ?: "Unknown error occurred"
                            showCostomeToast("Error:$errorMessage")
                        }
                    }
            }
        }

    }

    private fun  showCostomeToast(message: String){
        // Inflate the coustom layout
        val inflate = LayoutInflater.from(this)
        val layout = inflate.inflate(R.layout.custome_toast_inflate,null)


        //Set Custom Message

        val text: TextView = layout.findViewById(R.id.customToastText)
        text.text = message
        // Create the custom toast
        val customToast = Toast(applicationContext)
        customToast.duration = Toast.LENGTH_SHORT // Set the Duration of the toast
        customToast.view = layout //Use the custome layout'
        customToast.setGravity(Gravity.TOP,0,100)

        // show the custom toast
        customToast.show()
    }
}