package ru.netology.nmedia.adapter

import ru.netology.nmedia.dto.Post

interface PostInteractionListener {

    fun onLikeButtonClicked(post: Post)

    fun onShareButtonClicked(post: Post)

    fun onDeleteMenuOptionClicked(post: Post)

    fun onEditMenuOptionClicked(post: Post)

    fun onVideoPlayButtonClicked(post: Post)

    fun onVideoBannerClicked(post: Post)

}