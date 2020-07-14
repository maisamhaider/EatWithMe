package com.eatwithme.toori.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.eatwithme.toori.R
import kotlinx.android.synthetic.main.activity_messaging.*

class MessagingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging)

        messageSend_IV.setOnClickListener()
        {
            val message = message_TV.text.toString()
            if (message != "")
            {
                //😍❤❤❤❤❤❤🤣😂😁😉😉😉  jusk kidding// function is used to send message to user
                sendMessageToBeloved(message)
            }
            else
            {
                 val toast: Toast = Toast.makeText(this@MessagingActivity,"empty",Toast.LENGTH_LONG)
                toast.show()
            }

        }
    }

    private fun sendMessageToBeloved(message: String) {

    }
}