package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.hideKeyboard
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postAdapter = PostsAdapter(viewModel)
        binding.postsRecyclerView.adapter = postAdapter

        viewModel.data.observe(this) { posts ->
            postAdapter.submitList(posts)
        }

        binding.publishImageButton.setOnClickListener {
            with(binding) {
                val text = editTextField.text.toString()
                viewModel.onSaveButtonClicked(text)

                editTextField.clearFocus()
                editTextField.hideKeyboard()
                cancelEditingTextGroup.visibility = View.GONE
            }
        }

        viewModel.currentPost.observe(this) {
            binding.editTextField.setText(it?.content)
        }

        binding.cancelEditingImageButton.setOnClickListener {
            with(binding) {
                editTextField.clearFocus()
                editTextField.hideKeyboard()
                editTextField.text.clear()
                cancelEditingTextGroup.visibility = View.GONE
                viewModel.onCancelEditingButtonClicked()
            }
        }

        viewModel.cancelEditingPostViewVisibility.observe(this) {
            if (viewModel.cancelEditingPostViewVisibility.value != null) {
                binding.cancelEditingTextGroup.visibility = View.VISIBLE
            }
            viewModel.cancelEditingPostTextHint.observe(this) {
                if (viewModel.cancelEditingPostTextHint.value != null) {
                    binding.cancelEditingTextView.text = viewModel.cancelEditingPostTextHint.value
                }
            }
        }
    }
}