package myplayground.example.dicodingstory.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.databinding.StoryRowBinding
import myplayground.example.dicodingstory.model.Story
import myplayground.example.dicodingstory.util.DateTimeRelative

class StoryListAdapter(private val onClickListener: (v: View, story: Story) -> Unit = { _, _ -> }) :
    PagingDataAdapter<Story, StoryListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = StoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class ViewHolder(private val binding: StoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            Glide.with(binding.root).load(story.photoUrl)
                .into(binding.ivPostImage)
            binding.tvUsername.text = story.name
            binding.tvDescription.text = story.description

            binding.tvPostedAt.text =
                if (story.createdAt != null)
                    DateTimeRelative.parseTimeRelative(story.createdAt) else ""

            binding.clStory.setOnClickListener { v ->
                onClick(v, story)
            }
        }

        private fun onClick(v: View, story: Story) {
            when (v.id) {
                R.id.cl_story -> {
                    onClickListener(v, story)
                }

                else -> {}
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}