package com.example.chatapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.chatapp.databinding.ActivitySignupactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignupactivityBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var mDbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignupactivityBinding.inflate(layoutInflater)

        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
        binding.btnSignup2.setOnClickListener {
            var email= binding.edEmail.text.toString()
            var name = binding.edName.text.toString()
            var password = binding.edPassword.text.toString()
            signup(name,email,password)

        }
    }

    private fun signup(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    addUserToDataBase(name,email,auth.uid)
                    Toast.makeText(this, "success",Toast.LENGTH_LONG).show()
                    val intent= Intent(this@SignupActivity, MainActivity::class.java)
                   finish()
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun addUserToDataBase(name: String, email: String, uid: String?) {
      mDbRef= FirebaseDatabase.getInstance().getReference()
        if (uid != null) {
            mDbRef.child("user").child(uid).setValue(User(name,email,uid))
        }
    }
}