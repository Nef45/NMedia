package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.PostRepositoryInMemory
import ru.netology.nmedia.dto.Post

class PostViewModel : ViewModel(), PostInteractionListener {

    private val repository = PostRepositoryInMemory()

    val data by repository::data

    val currentPost = MutableLiveData<Post?>(null)

    val cancelEditingPostViewVisibility = MutableLiveData<Int?>(null)
    val cancelEditingPostTextHint = MutableLiveData<String?>(null)

    fun onSaveButtonClicked(text: String) {
        if (text.isBlank()) {
            return
        }

        val post = if (currentPost.value != null && cancelEditingPostViewVisibility.value != null) {
            currentPost.value!!.copy(
                content = text
            )
        } else {
            Post(
                id = PostRepository.NEW_POST_ID,
                author = "Me",
                content = text,
                published = "05.05.2022"
            )
        }

        repository.save(post)
        currentPost.value = null
        onCancelEditingButtonClicked()
    }

    fun onCancelEditingButtonClicked() {
        cancelEditingPostViewVisibility.value = null
        cancelEditingPostTextHint.value = null
    }

// region PostInteractionAdapter

    override fun onLikeButtonClicked(post: Post) {
        repository.like(post.id)
    }

    override fun onShareButtonClicked(post: Post) {
        repository.share(post.id)
    }

    override fun onDeleteMenuOptionClicked(post: Post) {
        repository.delete(post.id)
    }

    override fun onEditMenuOptionClicked(post: Post) {
        cancelEditingPostViewVisibility.value = 1
        cancelEditingPostTextHint.value = post.content
        currentPost.value = post
    }

// endregion PostInteractionAdapter

}