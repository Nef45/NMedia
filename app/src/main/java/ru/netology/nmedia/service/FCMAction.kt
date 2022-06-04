package ru.netology.nmedia.service

internal enum class FCMAction(
    val key: String
) {
    Like ("LIKE"),
    NewPost("POST");

    companion object {
        const val KEY_ACTION = "action"
        const val KEY_CONTENT = "content"
    }
}