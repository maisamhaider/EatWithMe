package com.eatwithme.toori.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.eatwithme.toori.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginAct_Toolbar: Toolbar = findViewById(R.id.loginAct_Toolbar)

        //setting chat tab layout as chat activity ToolBar
        setSupportActionBar(loginAct_Toolbar)
        supportActionBar!!.title = "Register" //!! not-null assertion..(!!)converts any value to a non-null type and throws an exception if the value is null
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        loginAct_Toolbar.setNavigationOnClickListener()
        {
            val intent: Intent = Intent(this@LoginActivity,WelcomeActivity::class.java )
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}