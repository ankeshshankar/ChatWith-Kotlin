package com.chatwith.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chatwith.app.R
import com.chatwith.app.model.Users
import com.chatwith.app.notify.LoadChat
import com.google.android.material.card.MaterialCardView
import org.greenrobot.eventbus.EventBus

class ChatListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val chatList = ArrayList<Users>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View? = null
        view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentChat = chatList[position]
        holder as ChatViewHolder
        holder.name.text = currentChat.username.toString()
        holder.email.text = currentChat.userEmail.toString()
        Glide.with(holder.itemView.context)
            .load(currentChat.imageUrl)
            .into(holder.userImage)
        holder.itemView.findViewById<MaterialCardView>(R.id.cardView).apply {
            setOnClickListener {
                val loadChat = LoadChat(
                    type = "CHAT",
                    name = currentChat.username.toString(),
                    receiverId = currentChat.uid.toString()
                )
                EventBus.getDefault().post(loadChat)
            }
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    fun setData(newList: ArrayList<Users>) {
        this.chatList.clear()
        this.chatList.addAll(newList)
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.chatterName)
        val email: TextView = itemView.findViewById(R.id.chatterEmail)
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
    }
}