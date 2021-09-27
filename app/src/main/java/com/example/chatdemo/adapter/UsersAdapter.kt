package com.example.chatdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chatdemo.databinding.UserRowLayoutBinding
import io.getstream.chat.android.client.models.User
import android.text.format.DateFormat
import android.util.Log
import androidx.navigation.findNavController
import com.example.chatdemo.ui.users.UsersFragmentDirections
import io.getstream.chat.android.client.ChatClient
import kotlin.math.log

class UsersAdapter : ListAdapter<User, UsersAdapter.ItemHolder>(DiffCallback()) {

    class ItemHolder(private val binding: UserRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val client = ChatClient.instance()

        fun bind(currentUser: User, holder: ItemHolder) {
            binding.apply {
                avatarImageView.setUserData(currentUser)
                usernameTextView.text = currentUser.id
                lastActiveTextView.text = convertDate(currentUser.lastActive!!.time)
                rootLayout.setOnClickListener {
                    createNewChannel(currentUser.id, holder)
                }
            }
        }

        private fun createNewChannel(selectedUser: String, holder: ItemHolder) {
            client.createChannel(
                channelType = "messaging",
                members = listOf(client.getCurrentUser()!!.id, selectedUser)
            ).enqueue { result ->
                if (result.isSuccess) {
                    navigateToChatFragment(holder, result.data().cid)
                } else {
                    Log.e("UsersAdapter", result.error().message.toString())
                }
            }
        }

        private fun navigateToChatFragment(holder: ItemHolder, cid: String) {
            val action = UsersFragmentDirections.actionUsersFragmentToChatFragment(cid)
            holder.itemView.findNavController().navigate(action)
        }

        private fun convertDate(milliseconds: Long): String {
            return DateFormat.format("dd/MM/yyyy hh:mm", milliseconds).toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            UserRowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position), holder)
    }

    private class DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: User, newItem: User) =
            oldItem == newItem
    }

    override fun submitList(list: MutableList<User>?) {
        super.submitList(list?.map { it.copy() })
    }
}