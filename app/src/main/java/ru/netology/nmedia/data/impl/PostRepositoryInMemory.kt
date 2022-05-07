package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemory : PostRepository {

    override val data = MutableLiveData(
        List(100) { index ->
            Post(
                100L - index,
                "Netology",
                "Text $index",
                "24.04.2022",
                999,
                false,
                9998,
                0
            )
        }
    )

    private val listOfPosts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    override fun like(postId: Long) {
        val newListOfPosts = listOfPosts.map {
            if (it.id == postId) {
                it.copy(
                    likedByMe = !it.likedByMe,
                    likes = if (!it.likedByMe) {
                        it.likes + 1
                    } else {
                        it.likes - 1
                    }
                )
            } else {
                it
            }
        }
        data.value = newListOfPosts
    }

    override fun share(postId: Long) {
        val newListOfPosts = listOfPosts.map {
            if (it.id == postId) {
                it.copy(shares = it.shares + 1)
            } else {
                it
            }
        }
        data.value = newListOfPosts
    }

    override fun delete(postId: Long) {
        data.value = listOfPosts.filter { it.id != postId }
    }

    override fun create(post: Post) {
        data.value = listOf(
            post.copy(
                id = listOfPosts.first().id + 1
            )
        ) + listOfPosts
    }

    override fun update(post: Post) {
        data.value = listOfPosts.map {
            if (it.id == post.id) {
                post
            } else {
                it
            }
        }
    }

    fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) {
            create(post)
        } else {
            update(post)
        }
    }
}
