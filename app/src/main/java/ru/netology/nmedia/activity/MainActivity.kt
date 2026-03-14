package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + v.paddingLeft,
                systemBars.top + v.paddingTop,
                systemBars.right + v.paddingRight,
                systemBars.bottom + v.paddingBottom
            )
            insets
        }

        val viewModel by viewModels<PostViewModel>()
        viewModel.data.observe(this) { post ->


            val formatSymbols = DecimalFormatSymbols(Locale.US).apply {
                decimalSeparator = '.'
            }

            val oneDecimalFormat = DecimalFormat("0.0", formatSymbols).apply {
                roundingMode = RoundingMode.DOWN
            }

            val noDecimalFormat = DecimalFormat("0", formatSymbols).apply {
                roundingMode = RoundingMode.DOWN
            }

            fun bigNumbersFormat(number: Int): String {
                when (number) {
                    in 0..999 -> {
                        return "$number"
                    }

                    in 1000..10_000 -> {
                        val value = number / 1000.0
                        val formatted = oneDecimalFormat.format(value)
                        return "${formatted}K"
                    }

                    in 10_000..999_999 -> {
                        val value = number / 1000.0
                        val formatted = noDecimalFormat.format(value)
                        return "${formatted}K"
                    }

                    else -> {
                        val value = number / 1_000_000.0
                        val formatted = oneDecimalFormat.format(value)
                        return "${formatted}M"
                    }
                }

            }

            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                likesCount.text = bigNumbersFormat(post.likes)
                sharesCount.text = bigNumbersFormat(post.reposts)
                avatarLikes.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24)
                avatarShares.setImageResource(R.drawable.ic_share_24)

            }
        }
        binding.avatarLikes.setOnClickListener {
            viewModel.like()
        }
        binding.avatarShares.setOnClickListener {
            viewModel.repost()
        }
    }
}