package com.example.chatdemo.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.chatdemo.R
import com.example.chatdemo.databinding.FragmentLoginBinding
import com.example.chatdemo.model.ChatUser
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    private val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        init()
        return binding.root
    }

    private fun init() {
        binding.button.setOnClickListener {
            authenticateUser()
        }
    }

    private fun authenticateUser() {
        binding.apply {
            val firstName = firstNameEditText.text.toString()
            val username = usernameEditText.text.toString()
            if (validateInput(firstName, firstNameInputLayout) &&
                validateInput(username, usernameInputLayout)
            ) {
                val chatUser = ChatUser(firstName, username)
                val action = LoginFragmentDirections.actionLoginFragmentToChannelFragment(chatUser)
                findNavController().navigate(action)
            }
        }
    }

    private fun validateInput(inputText: String, textInputLayout: TextInputLayout): Boolean {
        return if (inputText.length <= 3) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = "* Minimum 4 Characters Allowed"
            false
        } else {
            textInputLayout.isErrorEnabled = false
            textInputLayout.error = null
            true
        }
    }

}

















