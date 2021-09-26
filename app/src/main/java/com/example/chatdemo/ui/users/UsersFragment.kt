package com.example.chatdemo.ui.users

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatdemo.R
import com.example.chatdemo.adapter.UsersAdapter
import com.example.chatdemo.databinding.FragmentUsersBinding
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User

class UsersFragment : Fragment() {

    private val binding by lazy { FragmentUsersBinding.inflate(layoutInflater) }
    private val usersAdapter by lazy { UsersAdapter() }
    private val client = ChatClient.instance()
    private var usersList = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        init()
        return binding.root
    }

    private fun init() {
        binding.apply {
            setHasOptionsMenu(true)
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

            setupToolbar()
            setupRecyclerView()
            queryAllUsers()
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        binding.usersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.usersRecyclerView.adapter = usersAdapter
    }

    private fun queryAllUsers() {
        val request = QueryUsersRequest(
            filter = Filters.ne("id", client.getCurrentUser()!!.id),
            offset = 0,
            limit = 100
        )
        client.queryUsers(request).enqueue { result ->
            if (result.isSuccess) {
                val users: List<User> = result.data()
                usersList.addAll(users)
                usersAdapter.submitList(usersList)
            } else {
                Log.e("UsersFragment", result.error().message.toString())
            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//    }
}