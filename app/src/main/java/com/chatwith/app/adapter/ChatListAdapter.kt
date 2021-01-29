package com.chatwith.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chatwith.app.R
import com.chatwith.app.model.Chat
import com.google.android.material.card.MaterialCardView

class ChatListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val chatList = ArrayList<Chat>()

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
        /* Glide.with(this)
             .load(currentChat.imageUrl)
             .into(holder.userImage)*/
        holder.itemView.findViewById<MaterialCardView>(R.id.cardView).apply {
            setOnClickListener {

            }
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    fun setData(newList: ArrayList<Chat>) {
        this.chatList.clear()
        this.chatList.addAll(newList)
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.chatterName)
        val email: TextView = itemView.findViewById(R.id.chatterEmail)
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
    }
}