package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemory : PostRepository {

    private var initialPost = Post(
        0L,
        "Anna",
        "Text",
        "24.04.2022",
        999,
        false,
        9998,
        0
    )

    override val data = MutableLiveData(initialPost)

    override fun like() {
        val currentPost = data.value
        checkNotNull(currentPost) {
            "Data value should not be null"
        }
        val likedPost = currentPost.copy(
            likedByMe = !currentPost.likedByMe,
            likes = if (!currentPost.likedByMe) {
                currentPost.likes + 1
            } else {
                currentPost.likes - 1
            }
        )

        data.value = likedPost
    }

    override fun share() {
        val currentPost = data.value
        checkNotNull(currentPost) {
            "Data value should not be null"
        }
        val sharedPost = currentPost.copy(shares = currentPost.shares + 1)
        data.value = sharedPost
    }


}