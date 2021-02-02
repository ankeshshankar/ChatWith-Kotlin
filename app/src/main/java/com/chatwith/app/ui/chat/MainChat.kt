package com.chatwith.app.ui.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chatwith.app.adapter.MessageAdapter
import com.chatwith.app.databinding.ActivityMainChatBinding
import com.chatwith.app.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class MainChat : AppCompatActivity() {
    private lateinit var binding: ActivityMainChatBinding
    private val messageAdapter = MessageAdapter()
    private val messageList = arrayListOf<Message>()
    private lateinit var auth :FirebaseAuth
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainChatBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra("username")
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.userImage.setOnClickListener {

            if (binding.edtInput.text!!.isNotEmpty() || binding.edtInput.text!!.isNotBlank()) {
                val message = binding.edtInput.text.toString()
                val timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(Date())
                sendMessage(
                        sender = auth.currentUser?.uid.toString(),
                        receiver = intent.getStringExtra("receiverId").toString(),
                        message = message,
                        timestamp = timestamp
                )
                binding.edtInput.text!!.clear()
            } else {
                Toast.makeText(this, "Message not sent", Toast.LENGTH_SHORT).show()
            }

        }

        binding.newChatList.apply {
            this.layoutManager =
                LinearLayoutManager(
                        this.context,
                        LinearLayoutManager.VERTICAL,
                        false
                )
            this.itemAnimator = DefaultItemAnimator()
            this.adapter = messageAdapter

        }
        binding.newChatList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    //Scrolling down
                } else if (dy < 0) {
                    readMessage(
                            auth.currentUser?.uid.toString(),
                            intent.getStringExtra("receiverId").toString(),
                            "second")
                }
            }
        })











      /*      var scroll = false
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL -> {
                        scroll = true
                       *//* readMessage(
                                auth.currentUser?.uid.toString(),
                                intent.getStringExtra("receiverId").toString(),
                                "second"
                        )*//*

                    }
                }
            }
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {


                if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_SETTLING)
                    if(scroll){
                        readMessage(
                                auth.currentUser?.uid.toString(),
                                intent.getStringExtra("receiverId").toString(),
                                "second"
                        )
                    }
                scroll = false

                super.onScrolled(recyclerView, dx, dy)
            }*/
        readMessage(
                auth.currentUser?.uid.toString(),
                intent.getStringExtra("receiverId").toString(),
                "first"
        )

    }



    private fun readMessage(sender: String, receiver: String, time: String) {
        //val reference: DatabaseReference
       /* val rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
        val auth = FirebaseAuth.getInstance()
        reference = rootNode.getReference("Chat").child("messages")*/




        when(time){
            "first" -> {
                val reference = FirebaseDatabase.getInstance().reference
                        .child("Chat").child("messages")
                        .limitToLast(10)
                        .orderByChild("timestamp")
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        messageList.clear()
                        for (data in snapshot.children) {
                            val message = Message(
                                    sender = data.child("sender").value.toString(),
                                    receiver = data.child("receiver").value.toString(),
                                    message = data.child("message").value.toString(),
                                    timestamp = data.child("timestamp").value.toString(),
                            )
                            if (message.sender == sender && message.receiver == receiver || message.sender == receiver && message.receiver == sender) {
                                messageList.add(message)
                            }
                        }
                        messageAdapter.apply {
                            setData(messageList)
                            notifyDataSetChanged()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }
            "second" -> {
                val reference = FirebaseDatabase.getInstance().reference
                        .child("Chat").child("messages")
                        .limitToLast(10)
                        .orderByChild("timestamp")


                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (data in snapshot.children) {
                            val message = Message(
                                    sender = data.child("sender").value.toString(),
                                    receiver = data.child("receiver").value.toString(),
                                    message = data.child("message").value.toString(),
                                    timestamp = data.child("timestamp").value.toString(),
                            )
                            if (message.sender == sender && message.receiver == receiver || message.sender == receiver && message.receiver == sender) {
                                messageList.add(message)
                            }
                        }
                        messageAdapter.apply {
                            setData(messageList)
                            notifyDataSetChanged()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }
        }

    }


    private fun sendMessage(sender: String, receiver: String, message: String, timestamp: String) {
        val reference: DatabaseReference
        val rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
        reference = rootNode.getReference("Chat")
        val messageData = HashMap<String, Any>()
        messageData["sender"] = sender
        messageData["receiver"] = receiver
        messageData["message"] = message
        messageData["timestamp"] = timestamp

        reference.child("messages").push().setValue(messageData)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}