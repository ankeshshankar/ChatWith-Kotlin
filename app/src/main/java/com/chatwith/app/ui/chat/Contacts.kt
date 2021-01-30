package com.chatwith.app.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.chatwith.app.adapter.ContactLIstAdapter
import com.chatwith.app.databinding.ActivityContactsBinding
import com.chatwith.app.model.Users
import com.chatwith.app.notify.LoadChat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class Contacts : AppCompatActivity() {
    private lateinit var binding: ActivityContactsBinding
    private lateinit var auth: FirebaseAuth
    private val chatAdapter = ContactLIstAdapter()
    private val chatList = arrayListOf<Users>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Contacts"

        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
        binding.chatShimmer.visibility = View.VISIBLE
        binding.chatShimmer.startShimmer()
        readUser()

    }


    override fun onStart() {
        EventBus.getDefault().register(this)
        super.onStart()
    }


    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loadChat(event: LoadChat) {
        when (event.type) {
            "CONTACTS" -> {
                val intent = Intent(this, MainChat::class.java)
                intent.putExtra("username", event.name)
                intent.putExtra("receiverId", event.receiverId)
                startActivity(intent)
            }
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
        val reference: DatabaseReference
        val rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
        val auth = FirebaseAuth.getInstance()
        reference = rootNode.getReference("Users")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                /*  Log.e("data", snapshot.children.toString())*/
                for (data in snapshot.children) {
                    val user = Users(
                        uid = data.child("uid").value.toString(),
                        username = data.child("username").value.toString(),
                        userEmail = data.child("userEmail").value.toString(),
                        imageUrl = data.child("imageUrl").value.toString()
                    )
                    if (auth.currentUser?.uid != user.uid) {
                        chatList.add(user)
                    }

                }
                chatAdapter.apply {
                    setData(chatList)
                    binding.chatShimmer.visibility = View.GONE
                    binding.chatShimmer.stopShimmer()
                    notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}