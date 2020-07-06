package com.eatwithme.toori.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.eatwithme.toori.R
import com.google.android.material.tabs.TabLayout

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

         val registrationAct_Toolbar: Toolbar = findViewById(R.id.registrationAct_Toolbar)

        //setting chat tab layout as chat activity ToolBar
        setSupportActionBar(registrationAct_Toolbar)
        supportActionBar!!.title = "Register" //!! not-null assertion..(!!)converts any value to a non-null type and throws an exception if the value is null
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        registrationAct_Toolbar.setNavigationOnClickListener()
        {
            val intent: Intent = Intent(this@RegistrationActivity,WelcomeActivity::class.java )
            startActivity(intent)
            finish()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }
}