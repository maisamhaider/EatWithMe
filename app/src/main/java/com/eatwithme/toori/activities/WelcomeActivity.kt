package com.eatwithme.toori.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.eatwithme.toori.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class WelcomeActivity : AppCompatActivity() {

    var firebaseUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val welcome_register_BTN: Button = findViewById(R.id.welcome_register_BTN)
        val welcome_login_BTN: Button = findViewById(R.id.welcome_login_BTN)

        welcome_register_BTN.setOnClickListener()
        {
            val intent: Intent = Intent(this@WelcomeActivity,RegistrationActivity::class.java )
            startActivity(intent)
            finish()
        }
        welcome_login_BTN.setOnClickListener()
        {
            val intent: Intent = Intent(this@WelcomeActivity,LoginActivity::class.java )
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser !=null)
        {
            val intent: Intent = Intent(this@WelcomeActivity,EatWithMeMainActivity::class.java )
            startActivity(intent)
            finish()
        }
    }

}