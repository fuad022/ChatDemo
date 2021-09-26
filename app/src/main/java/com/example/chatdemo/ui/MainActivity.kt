package com.example.chatdemo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.chatdemo.R
import com.example.chatdemo.databinding.ActivityMainBinding
import com.example.chatdemo.model.ChatUser
import com.example.chatdemo.ui.login.LoginFragmentDirections
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.name

class MainActivity : AppCompatActivity() {

    //    private lateinit var navController: NavController
    private val client = ChatClient.instance()
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.apply {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
            val navController = navHostFragment.navController

            if (navController.currentDestination?.label.toString().contains("login")) {
                val currentUser = client.getCurrentUser()
                if (currentUser != null) {
                    val user = ChatUser(currentUser.name, currentUser.id)
                    val action = LoginFragmentDirections.actionLoginFragmentToChannelFragment(user)
                    navController.navigate(action)
                }
            }
        }
    }

}