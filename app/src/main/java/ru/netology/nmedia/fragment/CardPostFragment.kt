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
            viewModel.data.observe(viewLifecycleOwner) { posts ->
                val currentPost = posts.find { it.id == postId }

                currentPost?.let { post ->
                    with(binding) {
                        val formatNumbersHelper = FormatNumbers()

                        author.text = post.author
                        published.text = post.published
                        content.text = post.content

                        avatarLikes.isChecked = post.likedByMe
                        avatarLikes.text = formatNumbersHelper.bigNumbersFormat(post.likes)

                        avatarShares.isActivated = post.repostedByMe
                        avatarShares.text = formatNumbersHelper.bigNumbersFormat(post.reposts)


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
                            viewModel.likeById(post.id)
                        }

                        avatarShares.setOnClickListener {
                            viewModel.repostById(post.id)

                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, post.content)
                            }
                            val chooser = Intent.createChooser(
                                intent,
                                getString(R.string.description_post_shares)
                            )
                            startActivity(chooser)
                            viewModel.repostById(post.id)
                        }

                        fun openUrlInBrowser(url: String) {
                            if (url.isNotBlank()) {
                                val webpage = url.toUri()
                                val intent = Intent(Intent.ACTION_VIEW, webpage)

                                val packageManager: PackageManager = requireContext().packageManager

                                if (packageManager.resolveActivity(intent, 0) != null) {
                                    val chooser = Intent.createChooser(
                                        intent,
                                        getString(R.string.description_post_shares)
                                    )
                                    startActivity(chooser)
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Не могу открыть ссылку: нет подходящих приложений.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Ссылка пуста.",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }

                        fun videoShow(post: Post) {
                            post.video?.let { videoUrlString ->
                                openUrlInBrowser(videoUrlString)
                            }
                        }

                        val videoUrl = post.video
                        if (!videoUrl.isNullOrBlank()) {
                            editControlsGroup.visibility = View.VISIBLE

                            with(binding) {

                                videoContent.setOnClickListener {
                                    videoShow(post)
                                }
                                play.setOnClickListener {
                                    videoShow(post)
                                }
                            }
                        } else {
                            editControlsGroup.visibility = View.GONE
                        }

                    }

                }

            }

        }

        return binding.root
    }

}