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
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


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
                    LinearLayoutManager.VERTICAL,
                    false
                )
            this.itemAnimator = DefaultItemAnimator()
            this.adapter = chatAdapter

        }
        val chatList = arrayListOf<Users>()
        chatList.add(Users("aaa", "Ankesh Kumar", "kumar.ankeshshiv@gmail.com", "fdsafds"))
        chatList.add(Users("aaaa", "Sarin Kumar", "kumar.ankeshshiv@gmail.com", "fdsafds"))
        chatList.add(Users("aaaaa", "Santosh kumar", "kumar.ankeshshiv@gmail.com", "fdsafds"))

        chatAdapter.apply {
            setData(chatList)
            notifyDataSetChanged()
        }
        return binding.root
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
                val intent = Intent(requireActivity(), MainChat::class.java)
                intent.putExtra("username", event.name)
                startActivity(intent)
            }
            "CONTACTS" -> {
                val intent = Intent(requireActivity(), MainChat::class.java)
                intent.putExtra("username", event.name)
                startActivity(intent)
            }
        }
    }

}