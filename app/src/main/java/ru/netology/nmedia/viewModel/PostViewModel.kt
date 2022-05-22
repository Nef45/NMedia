package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.FilePostRepository
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel(application: Application) : AndroidViewModel(application),
    PostInteractionListener {

    private val repository = FilePostRepository(application)

    val data by repository::data

    val sharedPostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<String>()
    val videoUrl = SingleLiveEvent<String>()

    val currentPost = MutableLiveData<Post?>(null)

    val navigateToPostScreenEvent = SingleLiveEvent<Post>()
    val postToDisplay = MutableLiveData<Post?>(null)

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
                published = "Today"
            )
        }
        repository.save(post)
        currentPost.value = null
        postToDisplay.value = repository.findById(post.id)
    }

    fun onAddButtonClicked() {
        currentPost.value = null
        navigateToPostContentScreenEvent.call()
    }

// region PostInteractionAdapter

    override fun onLikeButtonClicked(post: Post) {
        repository.like(post.id)
        postToDisplay.value = repository.findById(post.id)
    }

    override fun onShareButtonClicked(post: Post) {
        sharedPostContent.value = post.content
        repository.share(post.id)
        postToDisplay.value = repository.findById(post.id)
    }

    override fun onDeleteMenuOptionClicked(post: Post) {
        repository.delete(post.id)
        postToDisplay.value = repository.findById(post.id)
    }

    override fun onEditMenuOptionClicked(post: Post) {
        currentPost.value = post
        navigateToPostContentScreenEvent.value = post.content
//        postToDisplay.value = repository.findById(post.id)
    }

    override fun onVideoPlayButtonClicked(post: Post) {
        videoUrl.value = post.videoUrl
    }

    override fun onVideoBannerClicked(post: Post) {
        videoUrl.value = post.videoUrl
    }

    override fun onPostClicked(post: Post) {
        navigateToPostScreenEvent.value = post
        postToDisplay.value = post
    }

// endregion PostInteractionAdapter

}