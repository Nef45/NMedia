package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModelStore
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) { post ->
            binding.fillPost(post)
        }

        binding.likesImageButton.setOnClickListener {
            viewModel.onLikeButtonClicked()
        }

        binding.shareImageButton.setOnClickListener {
            viewModel.onShareButtonClicked()
        }
    }

    private fun ActivityMainBinding.fillPost(post: Post) {
        authorNameTextView.text = post.author
        postText.text = post.content
        dateAndTimeTextView.text = post.published
        postLikes.text = post.likes.formatNumber()
        postShare.text = post.shares.formatNumber()
        postViews.text = post.views.toString()
        likesImageButton.setImageResource(getLikeIcon(post.likedByMe))
    }

    @DrawableRes
    private fun getLikeIcon(liked: Boolean): Int {
        return if (liked) {
            R.drawable.ic_likes_clicked_24dp
        } else {
            R.drawable.ic_likes_24dp
        }
    }

    private fun Int.formatNumber(): String {
        return when {
            this > 999_999 -> {
                (this / 1_000_000).toString() + "." + (this % 1_000_000 / 1_000).toString() + "M"
            }
            this > 9999 -> {
                (this / 1_000).toString() + "K"
            }
            this > 1099 -> {
                if (this % 1_000 < 100) {
                    (this / 1_000).toString() + "K"
                } else {
                    (this / 1_000).toString() + "." + (this % 1_000 / 100).toString() + "K"
                }
            }
            this > 999 -> {
                (this / 1_000).toString() + "K"
            }
            else -> {
                this.toString()
            }
        }
    }
}