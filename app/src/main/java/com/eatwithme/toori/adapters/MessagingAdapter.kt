package com.eatwithme.toori.adapters

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.eatwithme.toori.R
import com.eatwithme.toori.annotations.MyAnnotation
import com.eatwithme.toori.models.MessageModel
import com.eatwithme.toori.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MessagingAdapter(
    context: Context,
    messagingList: List<MessageModel>,
    imageUrl: String
) :
    RecyclerView.Adapter<MessagingAdapter.MessageItemViewHolder>() {

    private val context: Context
    private val messagingList: List<MessageModel>
    private var imageUrl: String
    private var firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!


    init {
        this.context = context
        this.messagingList = messagingList
        this.imageUrl = imageUrl
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageItemViewHolder {

        return if (viewType == 1) {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.sent_side_message_item_xml, parent, false)
            MessageItemViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.received_side_message_item_xml, parent, false)
            MessageItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: MessageItemViewHolder, position: Int) {
        val messageModel: MessageModel = messagingList[position]
        //if Image message
        Picasso.get().load(imageUrl).into(holder.messagingReceivedUSerProfile_CIV)
        if (messageModel.message_type == MyAnnotation.IMAGE) {

            if (messageModel.sender_id.equals(firebaseUser!!.uid)) {
                holder.textMessage_Tv!!.visibility = View.GONE
//                holder.sentImageMessage_IV!!.visibility = View.VISIBLE
//                Picasso.get().load(messageModel.message_image_url).into(holder.sentImageMessage_IV)

            }
            else if(!messageModel.sender_id.equals(firebaseUser!!.uid)) {
                holder.textMessage_Tv!!.visibility = View.GONE
//                holder.receivedImageMessage_IV!!.visibility = View.VISIBLE
//                Picasso.get().load(messageModel.message_image_url)
//                    .into(holder.receivedImageMessage_IV)
            }


        }
        //if text message
        else {
            holder.textMessage_Tv!!.text = messageModel.message
        }

        // for seed/sent feature
        if (position == messagingList.size - 1) {
            if (messageModel.is_seen) {
                holder.isSeen_TV!!.text = "Seen"
                if (messageModel.message_type.equals(MyAnnotation.IMAGE)) {
                    val param: ConstraintLayout.LayoutParams? =
                        holder.isSeen_TV!!.layoutParams as ConstraintLayout.LayoutParams?
                    param!!.setMargins(0, 245, 10, 0)
                    holder.isSeen_TV!!.layoutParams = param
                }

            } else {
                holder.isSeen_TV!!.text = "sent"
                if (messageModel.message_type.equals(MyAnnotation.IMAGE)) {
                    val param: ConstraintLayout.LayoutParams? =
                        holder.isSeen_TV!!.layoutParams as ConstraintLayout.LayoutParams?
                    param!!.setMargins(0, 245, 10, 0)
                    holder.isSeen_TV!!.layoutParams = param
                }

            }
        } else {
            holder.isSeen_TV!!.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return messagingList.size
    }

    inner class MessageItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var messagingReceivedUSerProfile_CIV: CircleImageView? = null
        var receivedImageMessage_IV: ImageView? =null
        var sentImageMessage_IV: ImageView? =null
        var textMessage_Tv: TextView? =null
        var isSeen_TV: TextView? =null


        init {
            messagingReceivedUSerProfile_CIV = itemView.findViewById(R.id.messagingReceivedUSerProfile_CIV)
            sentImageMessage_IV = itemView.findViewById(R.id.sentImageMessage_IV)
            receivedImageMessage_IV = itemView.findViewById(R.id.receivedImageMessage_IV)
            isSeen_TV = itemView.findViewById(R.id.isSeen_TV)
            textMessage_Tv = itemView.findViewById(R.id.textMessage_Tv)


        }

    }

    override fun getItemId(position: Int): Long {
        return if (messagingList[position].sender_id.equals(firebaseUser!!.uid)) {
            1
        } else {
            0
        }
    }

}