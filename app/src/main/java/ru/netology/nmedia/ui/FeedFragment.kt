package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FeedFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel

class FeedFragment : Fragment() {

    private val Fragment.packageManager
        get() = activity?.packageManager

    private val viewModel by activityViewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.sharedPostContent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(shareIntent)
        }

        setFragmentResultListener(requestKey = PostContentFragment.REQUEST_KEY_FEED) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY_FEED) return@setFragmentResultListener
            val newPostContent =
                bundle.getString(PostContentFragment.RESULT_KEY) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClicked(newPostContent)
        }

        viewModel.navigateToPostContentScreenEvent.observe(this) { initialContent ->
            val direction = FeedFragmentDirections.toPostContentFragment(initialContent, CALLER_FEED)
            findNavController().navigate(direction)
        }

        viewModel.videoUrl.observe(this) { link ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            if (packageManager?.let { intent.resolveActivity(it) } != null) {
                startActivity(intent)
            }
        }

        viewModel.navigateToPostScreenEvent.observe(this) { post ->
            val direction = FeedFragmentDirections.toSinglePostFragment(post.id)
            findNavController().navigate(direction)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
            val postAdapter = PostsAdapter(viewModel)
            binding.postsRecyclerView.adapter = postAdapter

            viewModel.data.observe(viewLifecycleOwner) { posts ->
                postAdapter.submitList(posts)
            }

            binding.newPostFab.setOnClickListener {
                viewModel.onAddButtonClicked()
            }

        }.root
    }

    companion object {
        const val CALLER_FEED = "CallerFeed"
    }
}
