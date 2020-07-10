package com.eatwithme.toori.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.eatwithme.toori.R
import com.eatwithme.toori.models.UserModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_chat_activty.*
import java.security.AccessControlContext

class ChatUsersAdaper(
    context: Context,
    chatList: List<UserModel>,
    isChecked: Boolean
) : RecyclerView.Adapter<ChatUsersAdaper.ChatUsersViewHolder>() {

    private val context:Context
    private val chatList : List<UserModel>
    private var isChecked : Boolean

    init {
        this.context = context
        this.chatList = chatList
        this.isChecked = isChecked
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatUsersViewHolder {

        val view: View = LayoutInflater.from(context).inflate(R.layout.chat_user_recyclerview_item_layout,parent,false)

    return ChatUsersAdaper.ChatUsersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ChatUsersViewHolder, position: Int) {
        val user: UserModel? = chatList[position]
        holder.chat_user_recyclerview_item_username_TV.text = user!!.username
        Picasso.get()
            .load(user!!.profile)
            .placeholder(R.drawable.ic_user)
            .into(holder.chat_user_recyclerview_item_profile_CIV)
    }


    class ChatUsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var chat_user_recyclerview_item_profile_CIV: CircleImageView
        var isActive_CIV: CircleImageView
        var chat_user_recyclerview_item_username_TV: TextView
        var last_msg_tv: TextView

        init {

            chat_user_recyclerview_item_profile_CIV =
                itemView.findViewById(R.id.chat_user_recyclerview_item_profile_CIV)
            isActive_CIV = itemView.findViewById(R.id.isActive_CIV)
            chat_user_recyclerview_item_username_TV =
                itemView.findViewById(R.id.chat_user_recyclerview_item_username_TV)
            last_msg_tv = itemView.findViewById(R.id.last_msg_tv)
        }

    }

}