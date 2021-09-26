package com.example.chatdemo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatUser(
    val firstname: String,
    val username: String
) : Parcelable