package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.UrlParse

internal class PostsAdapter(
    private val interactionListener: PostInteractionListener

) : ListAdapter<Post, PostsAdapter.PostsViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return PostsViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class PostsViewHolder(
        private val binding: PostBinding,
        private val interactionListener: PostInteractionListener

    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.postOptionsMaterialButton).apply {
                inflate(R.menu.post_options)
                setOnMenuItemClickListener { option ->
                    when (option.itemId) {
                        R.id.deletePostOption -> {
                            interactionListener.onDeleteMenuOptionClicked(post)
                            true
                        }
                        R.id.editPostOption -> {
                            interactionListener.onEditMenuOptionClicked(post)
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }
            }
        }

        init {
            binding.likesMaterialButton.setOnClickListener {
                interactionListener.onLikeButtonClicked(
                    post
                )
            }
            binding.shareMaterialButton.setOnClickListener {
                interactionListener.onShareButtonClicked(
                    post
                )
            }
            binding.postOptionsMaterialButton.setOnClickListener {
                popupMenu.show()
            }

            binding.videoPlayMaterialButton.setOnClickListener {
                interactionListener.onVideoPlayButtonClicked(
                    post
                )
            }

            binding.videoBannerImageButton.setOnClickListener {
                interactionListener.onVideoBannerClicked(
                    post
                )
            }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                authorNameTextView.text = post.author
                postText.text = post.content
                dateAndTimeTextView.text = post.published
                likesMaterialButton.text = post.likes.formatNumber()
                likesMaterialButton.isChecked = post.likedByMe
                shareMaterialButton.text = post.shares.formatNumber()
                shareMaterialButton.isChecked = post.sharedByMe
                postViews.text = post.views.toString()

                val urlList = UrlParse.getHyperLinks(postText.text.toString())
                for (link in urlList) {
                    if (link.contains("youtube")
                        ||
                        link.contains("youtu.be")
                    ) {
                        post.videoUrl = link
                    } else {
                        post.videoUrl = ""
                    }
                }
                if (post.videoUrl.isEmpty()) {
                    videoContentGroup.visibility = View.GONE
                } else {
                    videoContentGroup.visibility = View.VISIBLE
                }
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


    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return newItem == oldItem
        }

    }
}
