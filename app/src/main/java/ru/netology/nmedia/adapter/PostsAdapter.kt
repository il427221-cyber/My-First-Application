package ru.netology.nmedia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.FormatNumbers
import ru.netology.nmedia.databinding.FragmentCardPostBinding
import ru.netology.nmedia.dto.Post

interface PostListener {
    fun onEdit(post: Post)
    fun onRemove(post: Post)

    fun onLike(post: Post)

    fun onRepost(post: Post)

    fun onShow(post: Post)

    fun showPost(post: Post)
}

class PostsAdapter(private val listener: PostListener) : ListAdapter<Post, PostViewHolder>(
    PostViewHolder.PostDiffCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = FragmentCardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, listener)
    }

    override fun onBindViewHolder(viewHolder: PostViewHolder, position: Int) {
        val post = getItem(position)
        viewHolder.bind(post)
    }

}

class PostViewHolder(val binding: FragmentCardPostBinding, private val listener: PostListener) :
    RecyclerView.ViewHolder(binding.root) {
    val BASE_URL = "http://10.0.2.2:9999/api/slow/"
    fun ImageView.load(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_loading_100dp)
            .error(R.drawable.ic_error_100dp)
            .timeout(10_000)
            .into(this)
    }
    @SuppressLint("SetTextI18n")
    fun bind(post: Post) {
        with(binding) {
            val formatNumbersHelper = FormatNumbers()

           when(post.author) {
               "Сбер" -> author.text = "Сбер"
               "Тинькофф" -> author.text = "Тинькофф"
               "Netology" -> author.text = "Netology"
               else -> " "
           }

            when(post.author) {
                "Сбер" -> post.authorAvatar = "sber.jpg"
                "Тинькофф" -> post.authorAvatar = "tcs.jpg"
                "Netology" -> post.authorAvatar = "netology.jpg"
                else -> " "
            }

            published.text = post.published.toString()
            content.text = post.content

            binding.avatar.load("${BASE_URL}/avatars/${post.authorAvatar}")

            avatarLikes.isChecked = post.likedByMe
            avatarLikes.text = formatNumbersHelper.bigNumbersFormat(post.likes)

            content.setOnClickListener {
                listener.showPost(post)

            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_post)

                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                listener.onRemove(post)
                                true
                            }

                            R.id.edit -> {
                                listener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                    show()
                }
            }

            avatarLikes.setOnClickListener {
                listener.onLike(post)
            }

            avatarShares.setOnClickListener {
                listener.onRepost(post)
            }


        }
    }

    object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

    }
}