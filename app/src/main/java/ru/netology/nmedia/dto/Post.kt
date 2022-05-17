package ru.netology.nmedia.dto

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Int = 0,
    val likedByMe: Boolean = false,
    val shares: Int = 0,
    val sharedByMe: Boolean = false,
    val views: Int = 0,
) {
    var videoUrl: String = ""
}