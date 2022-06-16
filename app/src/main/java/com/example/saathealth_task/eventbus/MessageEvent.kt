package com.example.newapp.eventbus

data class MessageEvent(
    val str: String
) {
  fun getString(): String = str
}
