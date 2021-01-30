package com.chatwith.app.ui.bottom_menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.chatwith.app.adapter.ChatListAdapter
import com.chatwith.app.databinding.FragmentChatBinding
import com.chatwith.app.model.Users
import com.chatwith.app.notify.LoadChat
import com.chatwith.app.ui.chat.Contacts
import com.chatwith.app.ui.chat.MainChat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class Chat : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val chatList = arrayListOf<Users>()
    private val chatAdapter = ChatListAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(activity, Contacts::class.java))
        }

        binding.cartRecycle.apply {
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
        return binding.root
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
                    binding.chatShimmer.stopShimmer()
                    binding.chatShimmer.visibility = View.GONE
                    notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
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
            "CHAT" -> {
                val intent = Intent(requireContext(), MainChat::class.java)
                intent.putExtra("username", event.name)
                intent.putExtra("receiverId", event.receiverId)
                startActivity(intent)
            }
            "CONTACTS" -> {
                val intent = Intent(requireContext(), MainChat::class.java)
                intent.putExtra("username", event.name)
                intent.putExtra("receiverId", event.receiverId)
                startActivity(intent)
            }
        }
    }

}