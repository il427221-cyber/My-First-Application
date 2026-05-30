package ru.netology.nmedia.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.FormatNumbers
import ru.netology.nmedia.databinding.FragmentCardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class CardPostFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCardPostBinding.inflate(layoutInflater)

        val postId = arguments?.getLong("POST_ID")

        if (postId != null && postId != 0L) {
            viewModel.data.observe(viewLifecycleOwner) { state ->
                val currentPost = state.posts.find { it.id == postId }

                currentPost?.let { post ->
                    with(binding) {
                        val formatNumbersHelper = FormatNumbers()

                        author.text = post.author
                        published.text = post.published.toString()
                        content.text = post.content

                        avatarLikes.isChecked = post.likedByMe
                        avatarLikes.text = formatNumbersHelper.bigNumbersFormat(post.likes)

                        menu.setOnClickListener {
                            PopupMenu(it.context, it).apply {
                                inflate(R.menu.menu_post)

                                setOnMenuItemClickListener { item ->
                                    when (item.itemId) {
                                        R.id.remove -> {
                                            viewModel.removeById(post.id)
                                            findNavController().navigateUp()
                                            true
                                        }

                                        R.id.edit -> {
                                            viewModel.edit(post)
                                            findNavController().navigate(R.id.action_cardPostFragment_to_newPostFragment2)
                                            true
                                        }

                                        else -> false
                                    }
                                }
                                show()
                            }
                        }

                        avatarLikes.setOnClickListener {
                            viewModel.likeById(post)
                        }

                    }

                }

            }

        }

        return binding.root
    }

}