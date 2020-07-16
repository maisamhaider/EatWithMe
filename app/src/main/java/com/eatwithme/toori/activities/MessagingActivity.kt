package com.eatwithme.toori.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.eatwithme.toori.R
import com.eatwithme.toori.annotations.MyAnnotation
import com.eatwithme.toori.models.UserModel
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_messaging.*
import java.util.*
import kotlin.collections.HashMap

class MessagingActivity : AppCompatActivity() {
    private var targetUserId: String? = null
    private var senderFirebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging)

        intent = intent
        targetUserId = intent.getStringExtra(MyAnnotation.TARGET_USER_ID)
        senderFirebaseUser = FirebaseAuth.getInstance().currentUser
        val firebaseDatabaseReference = FirebaseDatabase.getInstance()
            .reference.child(MyAnnotation.USER).child(targetUserId!!)

        firebaseDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user: UserModel? = snapshot.getValue(UserModel::class.java)
                messaging_user_name_small_TV.text = user!!.username
                Picasso.get().load(user!!.profile).into(messaging_user_profile_CIV)

            }

        })

        // click on imageview to go to gallery to choose image;
        messagingSelectFile_iv.setOnClickListener()
        {

            var intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(
                Intent.createChooser(intent, "pick image"),
                MyAnnotation.REQ_CODE_1ST
            )
        }

        messageSend_IV.setOnClickListener()
        {
            val message = message_TV.text.toString()
            if (message != "") {
                //😍❤❤❤❤❤❤🤣😂😁😉😉😉  jusk kidding// function is used to send message to user// but love you all
                sendMessageToBeloved(message, senderFirebaseUser!!.uid, targetUserId!!)
                message_TV.setText("")
            } else {
                val toast: Toast =
                    Toast.makeText(this@MessagingActivity, "empty", Toast.LENGTH_LONG)
                toast.show()
            }

        }
    }

    private fun sendMessageToBeloved(
        message: String,
        senderFirebaseUserID: String,
        targetUser_Id: String
    ) {
        var firebaseDatabaseReference = FirebaseDatabase.getInstance().reference
        val messageUniqueKey = firebaseDatabaseReference.push().key

        val messageHashMap = HashMap<String, Any?>()
        messageHashMap[MyAnnotation.SENDER_ID] = senderFirebaseUserID
        messageHashMap[MyAnnotation.MESSAGE] = message
        messageHashMap[MyAnnotation.RECEIVER_ID] = targetUser_Id
        messageHashMap[MyAnnotation.IS_SEEN] = false
        messageHashMap[MyAnnotation.MESSAGE_IMAGE_URL] = ""
        messageHashMap[MyAnnotation.MESSAGE_KEY] = messageUniqueKey

        firebaseDatabaseReference.child(MyAnnotation.CHAT)
            .child(messageUniqueKey!!)
            .setValue(messageHashMap)
            .addOnCompleteListener()
            { task ->
                if (task.isSuccessful) {
                    val chatListSenderRef =
                        FirebaseDatabase.getInstance().reference.child(MyAnnotation.CHAT_LIST)
                            .child(senderFirebaseUser!!.uid)
                            .child(targetUser_Id)
                    chatListSenderRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists()) {
                                chatListSenderRef.child(MyAnnotation.ID)
                                    .setValue(senderFirebaseUser!!.uid)
                            }

                            val chatListReceiverRef =
                                FirebaseDatabase.getInstance().reference.child(MyAnnotation.CHAT_LIST)
                                    .child(targetUser_Id)
                                    .child(senderFirebaseUser!!.uid)

                            chatListReceiverRef.child(MyAnnotation.ID)
                                .setValue(senderFirebaseUser!!.uid)
                        }

                    })

                    // work on FCM (Firebase Cloud Messaging)
//                    firebaseDatabaseReference = FirebaseDatabase
//                        .getInstance().reference.child(MyAnnotation.USER).child(senderFirebaseUser!!.uid)

                }

            }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MyAnnotation.REQ_CODE_1ST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val progressBar = ProgressDialog(this)
            progressBar.setMessage("image is sending")
            progressBar.show()

            val chatImageUri = data.data
            val firebaseStorage =
                FirebaseStorage.getInstance().reference.child(MyAnnotation.CHAT_IMAGES)
            val reference = FirebaseDatabase.getInstance().reference
            val chatImageMsgKey = reference.push().key
            val chatImagesMsgPath = firebaseStorage.child("$chatImageMsgKey.jpg")

            var uploadTask: StorageTask<*>
            uploadTask = chatImagesMsgPath.putFile(chatImageUri!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }

                }
                return@Continuation chatImagesMsgPath.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val downloadedUrl = task.result
                    val uri = downloadedUrl.toString()
                    val imageMessageHashMap = HashMap<String, Any?>()
                    imageMessageHashMap[MyAnnotation.SENDER_ID] = senderFirebaseUser!!.uid
                    imageMessageHashMap[MyAnnotation.MESSAGE] = "Sent you an image"
                    imageMessageHashMap[MyAnnotation.RECEIVER_ID] = targetUserId
                    imageMessageHashMap[MyAnnotation.IS_SEEN] = false
                    imageMessageHashMap[MyAnnotation.MESSAGE_IMAGE_URL] = uri
                    imageMessageHashMap[MyAnnotation.MESSAGE_KEY] = chatImageMsgKey

                    reference.child(MyAnnotation.CHAT).child(chatImageMsgKey!!)
                        .setValue(imageMessageHashMap)
                    progressBar.cancel()

                }
            }
        }
    }


}