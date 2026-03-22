package ru.netology.nmedia.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostDiffCallback
import ru.netology.nmedia.adapter.PostListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel
import android.view.View
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

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
        val adapter = PostsAdapter(
            object : PostListener {
                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                    binding.editControlsGroup.visibility = View.VISIBLE

                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onRepost(post: Post) {
                    viewModel.repostById(post.id)
                }

            }

        )
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.save.setOnClickListener {
            val content = binding.contentPlate.text?.toString().orEmpty()

            if (content.isBlank()) {
                Toast.makeText(this, R.string.content_is_blank_error, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.saveContent(content)

            binding.contentPlate.setText(" ")
            binding.contentPlate.clearFocus()

            AndroidUtils.hideKeyboard(binding.contentPlate)
            binding.editControlsGroup.visibility = View.GONE
        }
        viewModel.edited.observe(this) { edited ->
            if (edited.id != 0L) {
                with(binding.contentPlate) {
                    AndroidUtils.showKeyboard(this)
                    setText("")
                    append(edited.content)
                }
            }
        }
        binding.cancelEdit.setOnClickListener {
            with(binding) {
                editControlsGroup.visibility = View.GONE
                AndroidUtils.hideKeyboard(binding.contentPlate)
                contentPlate.setText("")
                viewModel.clearEditMode()
            }
        }
    }
}