package ru.netology.nmedia.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import kotlin.getValue

class NewPostFragment : Fragment() {
    private val viewModel: PostViewModel by activityViewModels()

     lateinit var onBackPressedCallback: OnBackPressedCallback

    fun performSystemBack() {
        onBackPressedCallback.isEnabled = false
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    companion object {
        const val CONTENT_KEY = "CONTENT_KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(layoutInflater)


        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val content = binding.edit.text.toString()

                if (content.isNotBlank()) {
                    viewModel.saveDraft(content)
                    Toast.makeText(requireContext(), "Черновик сохранен", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.clearDraft()
                }
                performSystemBack()
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            val currentEditTextContent = binding.edit.text.toString()

            if (post != null && post.id != 0L) {
                if (currentEditTextContent != post.content) {
                    binding.edit.setText(post.content)
                    binding.edit.requestFocus()
                    binding.edit.text?.length?.let { selection ->
                        binding.edit.setSelection(selection)
                    }
                    Toast.makeText(context, "Редактируем пост: '${post.content}'", Toast.LENGTH_SHORT).show()
                }
            } else {
                val draftContent = viewModel.getDraft()

                if (draftContent != null && draftContent.isNotBlank()) {
                    if (currentEditTextContent != draftContent) {
                        binding.edit.setText(draftContent)
                        binding.edit.requestFocus()
                        binding.edit.text?.length?.let { selection ->
                            binding.edit.setSelection(selection)
                        }
                        Toast.makeText(context, "Загружен черновик: '${draftContent}'", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (currentEditTextContent.isNotBlank()) {
                        binding.edit.setText("")

                    }
                }
            }
        }

        binding.ok.setOnClickListener {
            if (!binding.edit.text.isNullOrBlank()) {
                val content = binding.edit.text.toString()
                viewModel.saveContent(content)
                viewModel.clearDraft()
            }
            findNavController().navigateUp()

        }


        return binding.root
    }


    override fun onStop() {
        super.onStop()
        viewModel.clearEditMode()
    }
}