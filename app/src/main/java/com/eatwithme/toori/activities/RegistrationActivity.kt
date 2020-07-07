package com.eatwithme.toori.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.eatwithme.toori.MyAnnotation
import com.eatwithme.toori.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    private lateinit var refFirebaseAuth: FirebaseAuth
    private lateinit var refUserDatabaseReference: DatabaseReference
    private var firebaseUserId: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val registrationAct_Toolbar: Toolbar = findViewById(R.id.registrationAct_Toolbar)

        //setting chat tab layout as chat activity ToolBar
        setSupportActionBar(registrationAct_Toolbar)
        supportActionBar!!.title =
            "Register" //!! not-null assertion..(!!)converts any value to a non-null type and throws an exception if the value is null
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        registrationAct_Toolbar.setNavigationOnClickListener()
        {
            val intent: Intent = Intent(this@RegistrationActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        refFirebaseAuth = FirebaseAuth.getInstance()
        register_BTN.setOnClickListener()
        {
            registrationFun()
        }


    }

    private fun registrationFun() {

        val userName: String = registrationUserName_ET.text.toString()
        val userEmail: String = registrationEmail_ET.text.toString()
        val userPassword: String = registrationPassword_ET.text.toString()

        // check some conditions if that satisfied then proceed process to create new user
        if (userName == "") {
            val toast: Toast = Toast.makeText(
                this@RegistrationActivity,
                "User name must not be empty",
                Toast.LENGTH_SHORT
            )
            toast.show()
        } else
            if (userEmail == "") {
                val toast: Toast = Toast.makeText(
                    this@RegistrationActivity,
                    "Email must not be empty",
                    Toast.LENGTH_SHORT
                )
                toast.show()

            } else
                if (!userEmail.contains("@") && !userEmail.contains(".com")) {
                    val toast: Toast = Toast.makeText(
                        this@RegistrationActivity,
                        "" + userEmail + "is not email address",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (userPassword == "") {
                    val toast: Toast = Toast.makeText(
                        this@RegistrationActivity,
                        "password must not be empty",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (userPassword.length < 8) {
                    val toast: Toast = Toast.makeText(
                        this@RegistrationActivity,
                        "at least 8 digits",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (userEmail.length > 40) {
                    val toast: Toast = Toast.makeText(
                        this@RegistrationActivity,
                        "below 40 digits",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else {
                    // above conditions are not in in given information : proceed
                    //create new user with Email and password
                    refFirebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener() { task ->
                            if (task.isSuccessful) {
                                //if user created Successful. take the current user id and store id in firebase database
                                firebaseUserId = refFirebaseAuth.currentUser!!.uid
                                refUserDatabaseReference = FirebaseDatabase.getInstance().reference.child(
                                    MyAnnotation.USER).child(firebaseUserId)

                                val userHashMap = HashMap<String, Any>()
                                userHashMap["uid"] = firebaseUserId
                                userHashMap["username"] = userName
                                userHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/eatwithme-83faf.appspot.com/o/ic_user.png?alt=media&token=7d90385f-feb0-4b8a-b49e-40c301936471"
                                userHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/eatwithme-83faf.appspot.com/o/ic_test_cover.png?alt=media&token=8e17d7e5-b195-46a3-a631-a76adf58c9e5"
                                userHashMap["status"] = "offline"
                                userHashMap["search"] = userName.toLowerCase()
                                userHashMap["facebook"] = "http://m.facebook.com"
                                userHashMap["instagram"] = "http://m.instagram.com"
                                userHashMap["website"] = "http://www.google.com"

                                refUserDatabaseReference.updateChildren(userHashMap)
                                    .addOnCompleteListener(){task ->
                                        if (task.isSuccessful)
                                        {
                                            val intent = Intent(this@RegistrationActivity,EatWithMeMainActivity::class.java)
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                            startActivity(intent)
                                            finish()
                                        }
                                        else
                                        {
                                            Toast.makeText(this@RegistrationActivity,"error"+task.exception!!.message,Toast.LENGTH_LONG).show()
                                        }
                                    }


                            } else {
                                //if some error accured during creating user
                                val toast: Toast = Toast.makeText(
                                    this@RegistrationActivity,
                                    "Error Message" + task.exception!!.message,
                                    Toast.LENGTH_SHORT
                                )
                                toast.show()
                            }
                        }
                }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}