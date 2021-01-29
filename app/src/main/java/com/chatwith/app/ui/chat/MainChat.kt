package com.chatwith.app.ui.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chatwith.app.databinding.ActivityMainChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MainChat : AppCompatActivity() {
    private lateinit var binding: ActivityMainChatBinding

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainChatBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra("username")
        setContentView(binding.root)

        binding.userImage.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            if (binding.edtInput.text!!.isNotEmpty() || binding.edtInput.text!!.isNotBlank()) {
                val message = binding.edtInput.text.toString()
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
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