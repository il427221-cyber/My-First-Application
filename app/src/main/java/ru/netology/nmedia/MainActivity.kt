package ru.netology.nmedia

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.ActivityMainBinding.inflate
import ru.netology.nmedia.dto.Post
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left + v.paddingLeft, systemBars.top + v.paddingTop, systemBars.right + v.paddingRight, systemBars.bottom + v.paddingBottom)
            insets
        }

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

        val post = Post(
            1,
            "Нетология. Университет интернет-профессий будущего",
            "21 мая в 18:36",
            "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb"
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likesCount.text = bigNumbersFormat(post.likes)
            sharesCount.text = bigNumbersFormat(post.reposts)

            avatarShares.setOnClickListener {
                post.reposts++
                post.repostedByMe = !post.repostedByMe
                avatarShares.setImageResource(R.drawable.ic_share_24)
                sharesCount.text = bigNumbersFormat(post.reposts)
            }

            if (post.likedByMe) {
                avatarLikes.setImageResource(R.drawable.ic_liked_24)
            }
            avatarLikes.setOnClickListener {
                if (post.likedByMe) post.likes-- else post.likes++
                post.likedByMe = !post.likedByMe
                avatarLikes.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24)
                likesCount.text = bigNumbersFormat(post.likes)
            }
        }
    }
}