package com.eatwithme.toori.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eatwithme.toori.R
import com.eatwithme.toori.adapters.MessagingAdapter
import com.eatwithme.toori.annotations.MyAnnotation
import com.eatwithme.toori.models.MessageModel
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
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MessagingActivity : AppCompatActivity() {
    private var targetUserId: String? = null
    private var senderFirebaseUser: FirebaseUser? = null
    private var messagingAdapter: MessagingAdapter? = null
    private var messageList: List<MessageModel>? = null
    private lateinit var messages_RV: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging)

        messages_RV = findViewById(R.id.messages_RV)
        var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.stackFromEnd = true
        messages_RV.layoutManager = linearLayoutManager

        intent = intent
        targetUserId = intent.getStringExtra(MyAnnotation.RECEIVER_ID)
        senderFirebaseUser = FirebaseAuth.getInstance().currentUser

        //firebase realtime database reference
        val firebaseDatabaseReference = FirebaseDatabase.getInstance()
            .reference.child(MyAnnotation.USER).child(targetUserId!!)

        firebaseDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user: UserModel? = snapshot.getValue(UserModel::class.java)
                messaging_user_name_small_TV.text = user!!.username
                Picasso.get().load(user.profile).into(messaging_user_profile_CIV)
                retrieveMessages(senderFirebaseUser!!.uid, targetUserId!!, user.profile)

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

    private fun retrieveMessages(senderUid: String, targetUser: String, imageUri: String) {

        messageList = ArrayList()

        val ref = FirebaseDatabase.getInstance().reference.child(MyAnnotation.CHAT)
        ref.addValueEventListener(object : ValueEventListener
        {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                (messageList as ArrayList<MessageModel>).clear()
                for (p0 in snapshot.children) {
                    val message = p0.getValue(MessageModel::class.java)

                    if (message!!.receiver_id.equals(senderUid)
                        && message!!.sender_id.equals(targetUser)
                        ||
                        message!!.receiver_id.equals(targetUser)
                        && message!!.sender_id.equals(senderUid))
                    {

                        (messageList as ArrayList<MessageModel>).add(message)

                    }
                    messagingAdapter = MessagingAdapter(this@MessagingActivity,messageList!!,imageUri)
                    messages_RV.adapter = messagingAdapter


                }
            }

        })

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
        messageHashMap[MyAnnotation.MESSAGE_TYPE] = MyAnnotation.TEXT
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
                                    .setValue(targetUser_Id)
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
            //
            val firebaseStorage =
                FirebaseStorage.getInstance().reference.child(MyAnnotation.CHAT_IMAGES)
            val reference = FirebaseDatabase.getInstance().reference
            val chatImageMsgKey =
                reference.push().key// creating unique key 😖 for chat image message
            val chatImagesMsgPath =
                firebaseStorage.child("$chatImageMsgKey.jpg")//saving message image with name as (unique key .jpg)

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
                    val url = downloadedUrl.toString()
                    val imageMessageHashMap = HashMap<String, Any?>()
                    imageMessageHashMap[MyAnnotation.SENDER_ID] = senderFirebaseUser!!.uid
                    imageMessageHashMap[MyAnnotation.MESSAGE] = ""
                    imageMessageHashMap[MyAnnotation.RECEIVER_ID] = targetUserId
                    imageMessageHashMap[MyAnnotation.MESSAGE_TYPE] = MyAnnotation.IMAGE
                    imageMessageHashMap[MyAnnotation.IS_SEEN] = false
                    imageMessageHashMap[MyAnnotation.MESSAGE_IMAGE_URL] = url
                    imageMessageHashMap[MyAnnotation.MESSAGE_KEY] = chatImageMsgKey

                    reference.child(MyAnnotation.CHAT).child(chatImageMsgKey!!)
                        .setValue(imageMessageHashMap)
                    progressBar.cancel()

                }
            }
        }
    }


}