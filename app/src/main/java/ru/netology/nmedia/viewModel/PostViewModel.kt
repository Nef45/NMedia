package ru.netology.nmedia.viewModel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.data.impl.PostRepositoryInMemory

class PostViewModel : ViewModel() {

    private val repository = PostRepositoryInMemory()

    val data by repository::data

    fun onLikeButtonClicked() {
        repository.like()
    }

    fun onShareButtonClicked() {
        repository.share()
    }
}