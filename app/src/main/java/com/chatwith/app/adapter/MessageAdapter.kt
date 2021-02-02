package com.chatwith.app.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chatwith.app.R
import com.chatwith.app.model.Message
import com.chatwith.app.notify.Scroll
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val RECEIVED = 0
    private val SEND = 1

    private val messageList = ArrayList<Message>()
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View? = null
        return if (viewType == SEND) {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_sent, parent, false)
            MessageViewHolderSender(view)
        } else {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_received, parent, false)
            MessageViewHolderReceiver(view)
        }

    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val lastPositions = -1
        val message = messageList[position]
        when (getItemViewType(position)) {
            RECEIVED -> {
                holder as MessageViewHolderReceiver
                holder.message.text = message.message
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                try {
                    val date = format.parse(message.timestamp)
                    holder.messageTime.text = "${date.hours}:${date.minutes}"
                } catch (e: Exception) {
                    println(e)
                }


            }
            SEND -> {
                holder as MessageViewHolderSender
                holder.message.text = message.message
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                try {
                    val date = format.parse(message.timestamp)
                    holder.messageTime.text = "${date.hours}:${date.minutes}"
                } catch (e: Exception) {
                    println(e)
                }
            }
        }


    }


    override fun getItemCount(): Int {
        return messageList.size
    }

    fun setData(newList: ArrayList<Message>) {
        this.messageList.clear()
        this.messageList.addAll(newList)
    }

    class MessageViewHolderSender(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.text_message_body)
        val messageTime: TextView = itemView.findViewById(R.id.text_message_time)
    }

    class MessageViewHolderReceiver(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.text_message_body)
        val messageTime: TextView = itemView.findViewById(R.id.text_message_time)
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        return if (messageList[position].sender == firebaseUser.uid) {
            SEND
        } else {
            RECEIVED
        }
    }


}