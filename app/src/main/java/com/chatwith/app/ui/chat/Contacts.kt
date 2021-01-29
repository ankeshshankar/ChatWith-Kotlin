package com.chatwith.app.ui.chat

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.chatwith.app.adapter.ChatListAdapter
import com.chatwith.app.databinding.ActivityContactsBinding
import com.chatwith.app.model.Chat
import com.google.firebase.auth.FirebaseAuth

class Contacts : AppCompatActivity() {
    private lateinit var binding: ActivityContactsBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Contacts"

        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatAdapter = ChatListAdapter()
        binding.newChatList.apply {
            this.layoutManager =
                LinearLayoutManager(
                    this.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            this.itemAnimator = DefaultItemAnimator()
            this.adapter = chatAdapter

        }
        val chatList = arrayListOf<Chat>()
        chatList.add(Chat("aaa", "Ankesh Kumar", "kumar.ankeshshiv@gmail.com", "fdsafds"))

        chatAdapter.apply {
            setData(chatList)
            notifyDataSetChanged()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun readUser() {

    }
}