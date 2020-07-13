package com.eatwithme.toori.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.UserHandle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.eatwithme.toori.R
import com.eatwithme.toori.annotations.MyAnnotation
import com.eatwithme.toori.models.UserModel
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_chat_seatings.view.*

class ChatSettingsFragment : Fragment() {

    private var userDatabaseReference: DatabaseReference? = null
    private var firebaseUser: FirebaseUser? = null
    private var storageReference: StorageReference? = null
    private var profileImageUri: Uri? = null
    private var coverImageUri: Uri? = null
    private val REQ_CODE = 1010
    private var isProfilePic: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_chat_seatings, container, false)

        //get instance of firebase database and current user
        firebaseUser = FirebaseAuth.getInstance().currentUser
        userDatabaseReference = FirebaseDatabase.getInstance().reference.child(MyAnnotation.USER)
            .child(firebaseUser!!.uid)

        userDatabaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    val user: UserModel? = snapshot.getValue(UserModel::class.java)
                    if (context != null) {
                        view.chatSettingsUsername_TV.text = user!!.username
                        Picasso.get().load(user!!.profile).into(view.chatSettingsProfile_CIV)
                        Picasso.get().load(user!!.cover).into(view.chatSettingsCover_IV)
                    }
                }
            }

        })

        view.chatSettingsCover_IV.setOnClickListener()
        {
            isProfilePic = false
            pickImage()
        }

        view.chatSettingsProfile_CIV.setOnClickListener()
        {
            isProfilePic = true
            pickImage()
        }

        return view
    }

    private fun pickImage() {
        var intent = Intent()
        intent.setType("image/*")
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, REQ_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE && resultCode == Activity.RESULT_OK && data!!.data != null) {

            if (isProfilePic) {
                profileImageUri = data.data
                uploadProfile(true)
            } else {
                coverImageUri = data.data
                uploadProfile(false)
            }
        }
    }

    private fun uploadProfile(isProfile: Boolean) {

        val progressBar = ProgressDialog(context)
        progressBar.setMessage("")

        if (isProfile) {
            storageReference = FirebaseStorage
                .getInstance()
                .reference
                .child(MyAnnotation.USER_PROFILE_IMAGES)

            val fileReference =
                storageReference!!.child(System.currentTimeMillis().toString() + ".jpg")

            var uploadTask: StorageTask<*>
            uploadTask = fileReference.putFile(profileImageUri!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }

                }
                return@Continuation fileReference.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val downloadedUrl = task.result
                    val uri = downloadedUrl.toString()
                    val profileHashMap = HashMap<String, Any>()
                    profileHashMap[MyAnnotation.PROFILE] = uri
                    userDatabaseReference!!.updateChildren(profileHashMap)

                }
            }

        } else {
            storageReference = FirebaseStorage
                .getInstance()
                .reference
                .child(MyAnnotation.USER_COVER_IMAGES)

            val fileReference =
                storageReference!!.child(System.currentTimeMillis().toString() + ".jpg")

            var uploadTask: StorageTask<*>
            uploadTask = fileReference.putFile(coverImageUri!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }

                }
                return@Continuation fileReference.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val downloadedUrl = task.result
                    val uri = downloadedUrl.toString()
                    val profileHashMap = HashMap<String, Any>()
                    profileHashMap[MyAnnotation.COVER] = uri
                    userDatabaseReference!!.updateChildren(profileHashMap)

                }
            }
        }
    }
}