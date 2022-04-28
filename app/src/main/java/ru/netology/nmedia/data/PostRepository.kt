package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: LiveData<Post>

    fun like()

    fun share()
}