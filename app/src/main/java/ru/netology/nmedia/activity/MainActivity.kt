package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel
import androidx.activity.result.launch
import androidx.core.net.toUri

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val viewModel: PostViewModel by viewModels()
        val newPostLauncher = registerForActivityResult(NewPostContract) {
            val result = it ?: return@registerForActivityResult
            viewModel.saveContent(result)
        }
        val newEditLauncher = registerForActivityResult(EditContract) { result ->
            if(result != null) {
                viewModel.saveContent(result)
            } else {
                viewModel.clearEditMode()
            }

        }

        val adapter = PostsAdapter(
            object : PostListener {
                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                    newEditLauncher.launch(post.content)

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

                        if (packageManager.resolveActivity(intent, 0) != null) {
                            val chooser = Intent.createChooser(
                                intent,
                                getString(R.string.description_post_shares)
                            ) // Используйте более общий текст
                            startActivity(chooser)
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Не могу открыть ссылку: нет подходящих приложений.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Ссылка пуста.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }


                override fun onShow(post: Post) {
                    post.video?.let { videoUrlString ->
                        openUrlInBrowser(videoUrlString)
                    } ?: run {
                        Toast.makeText(
                            this@MainActivity,
                            "В этом посте отсутствует видео",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }

        )

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.fab.setOnClickListener { newPostLauncher.launch() }

    }
}

