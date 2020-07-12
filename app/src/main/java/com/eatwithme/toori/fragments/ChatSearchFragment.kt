package com.eatwithme.toori.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eatwithme.toori.R
import com.eatwithme.toori.adapters.ChatUsersAdaper
import com.eatwithme.toori.annotations.MyAnnotation
import com.eatwithme.toori.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChatSearchFragment : Fragment() {

    private var chatUsersAdaper: ChatUsersAdaper? = null
    private var userModelList: List<UserModel>? = null
    private var linearLayoutManager: LinearLayoutManager?=null
    private var chatSearch_rv: RecyclerView? = null
    private var chatAllSearchUser_ET: EditText? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_chat_search, container, false)


        chatSearch_rv = view.findViewById(R.id.chatSearch_rv)
        chatAllSearchUser_ET = view.findViewById(R.id.chatAllSearchUser_ET)

        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager!!.orientation = LinearLayoutManager.VERTICAL
        chatSearch_rv!!.setHasFixedSize(true)
        chatSearch_rv!!.layoutManager = linearLayoutManager


        userModelList = ArrayList()
        retrieveUsersFun()

        chatAllSearchUser_ET!!.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchFun(p0.toString().toLowerCase())            }

        })
        return view
    }

   private fun retrieveUsersFun()
    {

        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val userDatabaseReference = FirebaseDatabase.getInstance().reference.child(MyAnnotation.USER)

        // get all users and put into userModelList and then give it to ChatUsersAdapter
        userDatabaseReference!!.addValueEventListener(object : ValueEventListener
        {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                    (userModelList as ArrayList<UserModel>).clear()

                if (chatAllSearchUser_ET!!.text.toString() == "")
                {

                    for (usr in snapshot.children) {
                        val user: UserModel? = usr.getValue(UserModel::class.java)
                        if (!(user!!.uid).equals(firebaseUserID))//ignor this user
                        {
                            (userModelList as ArrayList<UserModel>).add(user)
                        }
                    }
                    chatUsersAdaper = ChatUsersAdaper(context!!,userModelList!!,false)
                    chatSearch_rv!!.adapter = chatUsersAdaper
                }
            }


        })
    }

//

    private fun searchFun(string: String)
    {
        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val searchQuery = FirebaseDatabase.getInstance()
            .reference.child(MyAnnotation.USER)
            .orderByChild("search").startAt(string).endAt(string +"\uf8ff")

        searchQuery!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                (userModelList as ArrayList<UserModel>).clear()
                for (usr in snapshot.children) {
                    val user: UserModel? = usr.getValue(UserModel::class.java)
                    if (!(user!!.uid).equals(firebaseUserID))//ignor this user
                    {

                        (userModelList as ArrayList<UserModel>).add(user)
                    }
                }
                chatUsersAdaper = ChatUsersAdaper(context!!, userModelList!!, false)
                chatSearch_rv!!.adapter = chatUsersAdaper

            }})
        }
}
