package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.PostRepositoryInMemory
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel : ViewModel(), PostInteractionListener {

    private val repository = PostRepositoryInMemory()

    val data by repository::data

    val sharedPostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<Unit>()
    val videoUrl = SingleLiveEvent<String>()

    val currentPost = MutableLiveData<Post?>(null)


    fun onSaveButtonClicked(text: String) {
        if (text.isBlank()) {
            return
        }

        val post = if (currentPost.value != null) {
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
    }


    fun onAddButtonClicked() {
        currentPost.value = null
        navigateToPostContentScreenEvent.call()
    }

// region PostInteractionAdapter

    override fun onLikeButtonClicked(post: Post) {
        repository.like(post.id)
    }

    override fun onShareButtonClicked(post: Post) {
        sharedPostContent.value = post.content
        repository.share(post.id)
    }

    override fun onDeleteMenuOptionClicked(post: Post) {
        repository.delete(post.id)
    }

    override fun onEditMenuOptionClicked(post: Post) {
        currentPost.value = post
        navigateToPostContentScreenEvent.call()
    }

    override fun onVideoPlayButtonClicked(post: Post) {
        videoUrl.value = post.videoUrl
    }

    override fun onVideoBannerClicked(post: Post) {
        videoUrl.value = post.videoUrl
    }

// endregion PostInteractionAdapter

}