package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myPost = Post(
            0L,
            "Anna",
            "Text",
            "24.04.2022",
            999,
            false,
            9998,
            0
        )

        binding.fillPost(myPost)

        binding.likesImageButton.setOnClickListener {
            myPost.likedByMe = !myPost.likedByMe
            myPost.likes = getLikesCounter(myPost)
            binding.likesImageButton.setImageResource(getLikeIcon(myPost.likedByMe))
            binding.postLikes.text = myPost.likes.formatNumber()
        }

        binding.shareImageButton.setOnClickListener {
            myPost.shares = getSharesCounter(myPost)
            binding.postShare.text = myPost.shares.formatNumber()
        }
    }

    private fun ActivityMainBinding.fillPost(post: Post) {
        authorNameTextView.text = post.author
        postText.text = post.content
        dateAndTimeTextView.text = post.published
        postLikes.text = post.likes.toString()
        postShare.text = post.shares.toString()
        postViews.text = post.views.toString()
    }

    @DrawableRes
    private fun getLikeIcon(liked: Boolean): Int {
        return if (liked) {
            R.drawable.ic_likes_clicked_24dp
        } else {
            R.drawable.ic_likes_24dp
        }
    }

    private fun getLikesCounter(post: Post): Int {
        return if (post.likedByMe) {
            post.likes + 1
        } else {
            post.likes - 1
        }
    }

    private fun getSharesCounter(post: Post): Int {
        return post.shares + 1
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