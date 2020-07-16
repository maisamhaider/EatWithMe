package com.eatwithme.toori.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.view.menu.MenuView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.eatwithme.toori.R
import com.eatwithme.toori.activities.MessagingActivity
import com.eatwithme.toori.annotations.MyAnnotation
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

        holder.userChatItem_CL.setOnClickListener()
        {
            val options = arrayOf<CharSequence>(
                "Send Message",
                "Visit Profile")

            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                .setCancelable(true)
            builder.setItems(options,DialogInterface.OnClickListener{dialog, which ->

                if (which== 0)
                {
                    val intent : Intent = Intent(context,MessagingActivity::class.java)
                    intent.putExtra(MyAnnotation.TARGET_USER_ID, user.uid)
                    context.startActivity(intent)

                }else{
                    //TODO
                }

            })
            builder.show()

        }
    }


    class ChatUsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var chat_user_recyclerview_item_profile_CIV: CircleImageView
        var isActive_CIV: CircleImageView
        var chat_user_recyclerview_item_username_TV: TextView
        var last_msg_tv: TextView
        val userChatItem_CL: ConstraintLayout

        init {

            chat_user_recyclerview_item_profile_CIV =
                itemView.findViewById(R.id.chat_user_recyclerview_item_profile_CIV)
            isActive_CIV = itemView.findViewById(R.id.isActive_CIV)
            chat_user_recyclerview_item_username_TV =
                itemView.findViewById(R.id.chat_user_recyclerview_item_username_TV)
            last_msg_tv = itemView.findViewById(R.id.last_msg_tv)
            userChatItem_CL = itemView.findViewById(R.id.userChatItem_CL)
        }

    }

}