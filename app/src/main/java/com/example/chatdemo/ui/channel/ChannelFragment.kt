package com.example.chatdemo.ui.channel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chatdemo.R
import com.example.chatdemo.databinding.FragmentChannelBinding
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.ui.channel.list.header.viewmodel.ChannelListHeaderViewModel
import io.getstream.chat.android.ui.channel.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory

class ChannelFragment : Fragment() {

    private val binding by lazy { FragmentChannelBinding.inflate(layoutInflater) }
    private val args: ChannelFragmentArgs by navArgs()
    private val client = ChatClient.instance()
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        init()
        return binding.root
    }

    private fun init() {
        binding.apply {
            setupUser()
            setupChannels()

            channelListHeaderView.setOnActionButtonClickListener {
                findNavController().navigate(R.id.action_channelFragment_to_usersFragment)
            }

            channelsView.setChannelItemClickListener { channel ->
                val action =
                    ChannelFragmentDirections.actionChannelFragmentToChatFragment(channel.cid)
                findNavController().navigate(action)
            }
        }
    }

    private fun setupUser() {
        if (client.getCurrentUser() == null) {
            user = User(
                id = args.chatUser.username,
                extraData = mutableMapOf(
                    "name" to args.chatUser.firstname
                )
            )
            Log.d("ChannelAdapter", "User - " + user)
            val token = client.devToken(user.id)
            client.connectUser(
                user,
                token
            ).enqueue { result ->
                if (result.isSuccess) {
                    Log.d("ChannelFragment", "Success Connecting the User")
                } else {
                    Log.d("ChannelFragment", result.error().message.toString())
                }
            }
        }
    }

    private fun setupChannels() {
        binding.apply {
            val filters = Filters.and(
                Filters.eq("type", "messaging"),
                Filters.`in`("members", listOf(client.getCurrentUser()!!.id))
            )
            val viewModelFactory = ChannelListViewModelFactory(
                filters,
                ChannelListViewModel.DEFAULT_SORT
            )
            val listViewModel: ChannelListViewModel by viewModels { viewModelFactory }
            val listHeaderViewModel: ChannelListHeaderViewModel by viewModels()

            listHeaderViewModel.bindView(channelListHeaderView, viewLifecycleOwner)
            listViewModel.bindView(channelsView, viewLifecycleOwner)
        }
    }

}












