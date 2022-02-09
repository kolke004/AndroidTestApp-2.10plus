package com.example.androidtestapp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtestapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import sdk.pendo.io.Pendo


class LoginActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding:ActivityLoginBinding
    //ActionBar
    private lateinit var actionBar: ActionBar
    //ProgressDialog
    private lateinit var progressDialog:ProgressDialog
    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // !!DEPRECATED PENDO 2.9 and older method!! //
        //PENDO AppKey init
//        val pendoAppKey =
//            "eyJhbGciOiJSUzI1NiIsImtpZCI6IiIsInR5cCI6IkpXVCJ9.eyJkYXRhY2VudGVyIjoidXMiLCJrZXkiOiJkNGYxMGE3NDI2ZjU2MTZjOGIwOTc2NmNlYjRmOGI5YzkzZTQyOTY2NWRjYjhlNjA2MmRmODJmNjQ3NDBkZTIxM2ZiZmQxMzQ3NjljNjZhYzA2YTljNmMxZTlkYTQ5MGVjMWQ4M2YwY2FjMDY5YWRiNDNiZWRiNjM3NDY3ZjEwMzRiNDYzZGEzMGU1YzJmOWJjYjYyMTU1MjFjMzYxOWQ5LjQ4M2ViYTg5MGU1ZjFlZjVkNzMxYzE5ZTQyMDAyNDRmLmYzM2U5OWYwYzc0OGI4OTQzNmIzODhmNjBmMDE2NTZmNDJhZDQ0MWJmZWNlNmI0MDRhOTJiNTg1NmNiOTczOTQifQ.DnsjIMt-MNCrC0LIIhKOehbJnd5Ik9LyvUgOSncIRjsNvH0N1ZIJgVKUFSj26vFuyshgy_lmV2N8dGrfnQ9JnUdFKaeZ7Y-ETy__s-HoCH6FHMAe1mBxj868aeUexGA7hRViZlt0l27J9HOkFYWDzH-KPVF83cRFXmxAIc0BOAk"
//        Pendo.initSdkWithoutVisitor(
//            this,
//            pendoAppKey,
//            null
//        )

        // PENDO 2.10+ methods //
        //PENDO APPKEY INIT STATEMENT//
        val pendoAppKey =
            "eyJhbGciOiJSUzI1NiIsImtpZCI6IiIsInR5cCI6IkpXVCJ9.eyJkYXRhY2VudGVyIjoidXMiLCJrZXkiOiI5MzkzNDhiNzJmYjA0NmNlODI0MGNmOWNkZjY2NGE2MjhiMGQ2ZTNmYTgzNzU0OTMwYWUyYTIzOGE1YWNkYWQ0MjNjOTlmZDMwMzViYzEwNTEzN2M5YjlhNjA4NTY3YTk0YWE0OGY3YmRmM2ZlYzNlNjZlNDU3M2FhYjM1ZTUyMGYwZmVkY2I5MGY3ODc3ZjZhOWRhMDlkNmRmM2VmZGNkLjZlZjZmZjhiNTJiYTEwMzg3OWNjMGZjNzQyZWM5NzA2LmY1ZWIyNGE5YjI3Y2Q2NzA2ZmUyM2I5ZWE1NmM4ZjZhMmM0ZjlmYTRmMWYxMGJkM2IxODEyMTljMDQ2YjIyYmIifQ.bY25BZiv3oJr_CxnFQmWAHYz8P1IaDTupWDVxbFR37mSPHxvDHkrAIOmJPFE9pOnve7LljHxBKnrGff2737Z7_5ilPuLrMLzvIE47wwW3SMM0CyNrBBO11lebipEqE_TYr2VExZoH1ro1brldcm6-uuRSXGOPEIM6AZSB3qohEY"

        Pendo.setup(
            application,
            pendoAppKey,
            null,
            null
        )
        // PENDO 2.10+ methods //

        //configure actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle click, open register activity
        binding.noAccountTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        //handle click, begin login
        binding.loginBtn.setOnClickListener {
            //before logging in, validate data
            validateData()
        }
    }

    private fun validateData() {
        //get data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.emailEt.setError("Invalid Email Format")
        }
        else if (TextUtils.isEmpty(password)){
            //no password entered
            binding.passwordEt.error = "Please Enter Password"
        }
        else{
            //data is validated, begin login
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        //show progress
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //login success
                progressDialog.dismiss()
                //get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "LoggedIn as $email", Toast.LENGTH_SHORT).show()

                //open profile
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            .addOnFailureListener { e->
                //login failed
                progressDialog.dismiss()
                Toast.makeText(this, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        //if user is alreay logged in, go to profile activity
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            //user is already logged in
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }
}