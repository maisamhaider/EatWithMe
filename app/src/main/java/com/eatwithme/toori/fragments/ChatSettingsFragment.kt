package com.eatwithme.toori.fragments

import android.os.Bundle
import android.os.UserHandle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eatwithme.toori.R
import com.eatwithme.toori.annotations.MyAnnotation
import com.eatwithme.toori.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_chat_seatings.view.*

class ChatSettingsFragment : Fragment() {

    var userDatabaseReference: DatabaseReference? = null
    var firebaseUser: FirebaseUser?= null

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
        userDatabaseReference = FirebaseDatabase.getInstance().reference.child(MyAnnotation.USER).child(firebaseUser!!.uid)

        userDatabaseReference!!.addValueEventListener(object : ValueEventListener
        {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists())
                {
                    val user: UserModel? = snapshot.getValue(UserModel::class.java)
                    if (context !=null)
                    { view.chatSettingsUsername_TV.text = user!!.username
                       Picasso.get().load(user!!.profile).into(view.chatSettingsProfile_CIV)
                       Picasso.get().load(user!!.cover).into(view.chatSettingsCover_IV)
                    }
                }
            }

        })
        return view
    }
}