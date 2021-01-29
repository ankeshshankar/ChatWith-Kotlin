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
import com.chatwith.app.model.Chat
import com.chatwith.app.ui.chat.Contacts

class Chat : Fragment() {
    private lateinit var binding: FragmentChatBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(activity, Contacts::class.java))
        }
        val chatAdapter = ChatListAdapter()
        binding.cartRecycle.apply {
            this.layoutManager =
                LinearLayoutManager(
                    this.context,
                    LinearLayoutManager.HORIZONTAL,
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
        return binding.root
    }

}