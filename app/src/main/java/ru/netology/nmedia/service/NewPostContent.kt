package ru.netology.nmedia.service

import com.google.gson.annotations.SerializedName

class NewPostContent(

    @SerializedName("newPostId")
    val newPostId: Long,

    @SerializedName("newPostAuthor")
    val newPostAuthor: String,

    @SerializedName("newPostContent")
    val newPostContent: String

) {
}