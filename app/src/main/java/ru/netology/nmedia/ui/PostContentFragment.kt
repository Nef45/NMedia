package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.PostContentFragmentBinding

class PostContentFragment : Fragment() {

    private val initialContent by lazy {
        val args by navArgs<PostContentFragmentArgs>()
        args.initialContent
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return PostContentFragmentBinding.inflate(layoutInflater, container, false)
            .also { binding ->
                binding.edit.setText(initialContent)
                binding.edit.requestFocus()

                binding.ok.setOnClickListener {
                    if (!binding.edit.text.isNullOrBlank()) {
                        val resultBundle = Bundle(1)
                        resultBundle.putString(RESULT_KEY, binding.edit.text.toString())
                        setFragmentResult(REQUEST_KEY, resultBundle)
                    }
                    findNavController().popBackStack()
                }
            }.root
    }

    companion object {
        const val RESULT_KEY = "postNewContent"
        const val REQUEST_KEY = "requestKey"
    }
}