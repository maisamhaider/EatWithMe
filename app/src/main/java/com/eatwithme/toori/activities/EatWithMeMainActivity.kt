package com.eatwithme.toori.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.eatwithme.toori.R

class EatWithMeMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eat_with_me_main)

        val chatAvtivity_BTN: Button = findViewById(R.id.chat_activity_BTN)

        chatAvtivity_BTN.setOnClickListener()
        {
            val intent: Intent = Intent(this,ChatActivty::class.java)
            startActivity(intent)
        }

    }
}