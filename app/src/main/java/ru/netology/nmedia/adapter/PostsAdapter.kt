package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.FormatNumbers
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

interface PostListener {
    fun onEdit(post: Post)
    fun onRemove(post: Post)

    fun onLike(post: Post)

    fun onRepost(post: Post)

    fun onShow(post: Post)
}

class PostsAdapter(private val listener: PostListener) : ListAdapter<Post, PostViewHolder>(
    PostViewHolder.PostDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, listener)
    }

    override fun onBindViewHolder(viewHolder: PostViewHolder, position: Int) {
        val post = getItem(position)
        viewHolder.bind(post)
    }

}

class PostViewHolder(val binding: CardPostBinding, private val listener: PostListener) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
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

            val videoUrl = post.video
            if (!videoUrl.isNullOrBlank()) {
                editControlsGroup.visibility = View.VISIBLE

                binding.videoContent.setOnClickListener {
                    listener.onShow(post)
                }
                binding.play.setOnClickListener {
                    listener.onShow(post)
                }

            }
        }
    }

    object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

    }
}