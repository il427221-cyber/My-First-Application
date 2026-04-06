package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import kotlin.getValue

class NewPostFragment : Fragment() {
    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewPostBinding.inflate(layoutInflater)

        viewModel.edited.observe(viewLifecycleOwner) { value ->
            if (value != null) {
                with(binding) {
                    edit.setText(value.content)
                    edit.requestFocus()
                }

                } else {
                    binding.edit.setText("")
                }
            }


        val text: String? = arguments?.getString(CONTENT_KEY)
        binding.edit.setText(text)

        binding.ok.setOnClickListener {
            if (!binding.edit.text.isNullOrBlank()) {
                val content = binding.edit.text.toString()
                viewModel.saveContent(content)
            }
                findNavController().navigateUp()

        }










        return binding.root
    }

    companion object {
        const val CONTENT_KEY = "CONTENT_KEY"
    }


    override fun onStop() {
        super.onStop()
        viewModel.clearEditMode()
    }
}