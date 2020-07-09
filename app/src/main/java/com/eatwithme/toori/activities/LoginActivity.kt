package com.eatwithme.toori.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.eatwithme.toori.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*

class LoginActivity : AppCompatActivity() {

    private lateinit var refFirebaseAuth: FirebaseAuth
    private lateinit var refUserDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        refFirebaseAuth = FirebaseAuth.getInstance()

        val loginAct_Toolbar: Toolbar = findViewById(R.id.loginAct_Toolbar)

        //setting chat tab layout as chat activity ToolBar
        setSupportActionBar(loginAct_Toolbar)
        supportActionBar!!.title =
            "Login" //!! not-null assertion..(!!)converts any value to a non-null type and throws an exception if the value is null
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        loginAct_Toolbar.setNavigationOnClickListener()
        {
            val intent: Intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        login_BTN.setOnClickListener()
        {
            loginFun()
        }


    }

    private fun loginFun() {

        val userEmail: String = LoginEmail_ET.text.toString()
        val userPassword: String = loginPassword_ET.text.toString()

        // check some conditions if that satisfied then proceed process to create new user
        if (userEmail == "") {
            val toast: Toast = Toast.makeText(
                this@LoginActivity,
                "User name must not be empty",
                Toast.LENGTH_SHORT
            )
            toast.show()
        } else
            if (userPassword == "") {
                val toast: Toast = Toast.makeText(
                    this@LoginActivity,
                    "password must not be empty",
                    Toast.LENGTH_SHORT
                )
                toast.show()

            } else
            {
                refFirebaseAuth.signInWithEmailAndPassword(userEmail,userPassword)
                    .addOnCompleteListener(){task ->
                        if (task.isSuccessful)
                        {
                            // if successful ❤ then go to EatWithMe Main activity
                            val intent = Intent(this@LoginActivity,EatWithMeMainActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            finish()
                        }
                        else
                        {
                            // error 😈👿👹👺💀☠👻👽👾🤖💩
                           Toast.makeText(this@LoginActivity,"error"+task.exception!!.message,Toast.LENGTH_LONG).show()
                        }
                    }

            }
     }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}