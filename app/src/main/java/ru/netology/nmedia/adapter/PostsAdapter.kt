package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.FormatNumbers
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

typealias LikeListener = (Post) -> Unit
typealias RepostListener = (Post) -> Unit

class PostsAdapter(private val likeListener: LikeListener, private val repostListener: RepostListener): ListAdapter<Post, PostViewHolder>(
    PostDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, likeListener, repostListener)
    }

    override fun onBindViewHolder(viewHolder: PostViewHolder, position: Int) {
        val post = getItem(position)
        viewHolder.bind(post)
    }

}

class PostViewHolder(val binding: CardPostBinding, private val likeListener: LikeListener, private val repostListener: RepostListener)
    : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        with(binding) {
            val formatNumbersHelper = FormatNumbers()

            author.text = post.author
            published.text = post.published
            content.text = post.content
            likesCount.text = formatNumbersHelper.bigNumbersFormat(post.likes)
            sharesCount.text = formatNumbersHelper.bigNumbersFormat(post.reposts)
            avatarLikes.setImageResource(
                if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
            )
            avatarShares.setImageResource(
                R.drawable.ic_share_24
            )

            avatarLikes.setOnClickListener {
                likeListener(post)
            }

            avatarShares.setOnClickListener {
                repostListener(post)
            }
        }
    }
}

object PostDiffCallback: DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

}