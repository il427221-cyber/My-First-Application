package ru.netology.nmedia.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.fragment.NewPostFragment.Companion.CONTENT_KEY
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(layoutInflater)


        val content: String? = arguments?.getString(CONTENT_KEY)

        val viewModel: PostViewModel by activityViewModels()

        val adapter = PostsAdapter(
            object : PostListener {
                override fun onEdit(post: Post) {

                    viewModel.edit(post)
                    findNavController().navigate(R.id.action_feedFragment4_to_newPostFragment2)

                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onRepost(post: Post) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, post.content)
                    }
                    val chooser =
                        Intent.createChooser(intent, getString(R.string.description_post_shares))
                    startActivity(chooser)
                    viewModel.repostById(post.id)
                }

                private fun openUrlInBrowser(url: String) {
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
                        Toast.makeText(requireContext(), "Ссылка пуста.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onShow(post: Post) {
                    post.video?.let { videoUrlString ->
                        openUrlInBrowser(videoUrlString)
                    } ?: run {
                        Toast.makeText(
                            requireContext(),
                            "В этом посте отсутствует видео",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun showPost(post: Post) {
                    val bundle = Bundle().apply {
                        putLong("POST_ID", post.id)
                    }
                    findNavController().navigate(
                        R.id.action_feedFragment4_to_cardPostFragment,
                        bundle
                    )
                }

            }

        )

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment4_to_newPostFragment2)
        }



        return binding.root
    }


}

