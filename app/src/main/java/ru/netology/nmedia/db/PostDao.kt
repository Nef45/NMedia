package ru.netology.nmedia.db

import ru.netology.nmedia.dto.Post

interface PostDao {
    fun getAll(): List<Post>

    fun save(post: Post): Post

    fun like(postId: Long)

    fun share(postId: Long)

    fun delete(postId: Long)


}