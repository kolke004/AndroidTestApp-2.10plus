package com.example.androidtestapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtestapp.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import sdk.pendo.io.Pendo


class ProfileActivity : AppCompatActivity() {
    //ViewBinding
    private lateinit var binding: ActivityProfileBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

// !!PENDO 2.10+ METHODS!! //
        // !!SETS VISITOR + ACCOUNT IDS!! //
        val visitorId = FirebaseAuth.getInstance().currentUser?.uid
        val accountId = "Test Mobile"

        // !!SETS VARIABLES FOR METADATA FIELDS!! //
        val email = FirebaseAuth.getInstance().currentUser?.email
        val isVerified = FirebaseAuth.getInstance().currentUser?.isEmailVerified
        val displayName = FirebaseAuth.getInstance().currentUser?.displayName

        // !!SETS VISITOR METADATA VALUES!! //
        val visitorData: HashMap<String, Any> = HashMap()
        visitorData["email"] = email.toString()
        visitorData["isVerified"] = isVerified.toString()
        visitorData["displayName"]  = displayName.toString()

        // !!SETS ACCOUNT METADATA VALUES!! //
        val accountData: HashMap<String, Any> = HashMap()
        accountData["mobileAccountData"] = "placeholder"

        // !!STARTS SESSION AND PASSES DATA TO PENDO!! //
        Pendo.startSession(
            visitorId,
            accountId,
            visitorData,
            accountData
        )
// !!PENDO 2.10+ METHODS!! //

// !!!WARNING DEPRECATED PENDO 2.9 AND OLDER METHODS!!! //
////PENDO METADATA//
//        //send Firebase visitor/accountIds to Pendo
//        val visitorId = FirebaseAuth.getInstance().currentUser!!.uid
//        val accountId = "TestMobile"
//// DO NOT UNCOMMENT
////        val email = FirebaseAuth.getInstance().currentUser!!.email
//
//        val pendoParams = PendoInitParams()
//        pendoParams.visitorId = visitorId
//        pendoParams.accountId = accountId
//
//
//        val visitorData: MutableMap<String, Any> = HashMap()
//// DO NOT UNCOMMENT
////        visitorData["email"] = email
//        visitorData["country"] = "USA"
//        pendoParams.visitorData = visitorData
////PENDO METADATA//
// !!!WARNING DEPRECATED PENDO 2.9 AND OLDER METHODS!!! //

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure ActionBar
        actionBar = supportActionBar!!
        actionBar.title = "Profile"

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle click, logout
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

        binding.mainActivityBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun checkUser() {
        //check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            //user not null, user logged in, get user info
            val email = firebaseUser.email
            //set to text view
            binding.emailTv.text = email
        }
        else{
            //user is null is not logged in, go to login activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}