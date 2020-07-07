package com.eatwithme.toori.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.eatwithme.toori.R
import com.google.firebase.auth.FirebaseAuth

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
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.groupId)
        {
           R.id.action_logout ->
           {
            FirebaseAuth.getInstance().signOut()
               val intent = Intent(this@EatWithMeMainActivity,WelcomeActivity::class.java)
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
               startActivity(intent)
               finish()

               return true
           }
        }
        return false

    }
}