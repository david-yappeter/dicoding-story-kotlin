package myplayground.example.dicodingstory.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.model.Story
import myplayground.example.dicodingstory.util.DateTimeRelative

class StoryListAdapter(private val onClickListener: (v: View, story: Story) -> Unit = { _, _ -> }) :
    PagingDataAdapter<Story, StoryListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.story_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if(data != null) {
            holder.bind(data)
        }
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(story: Story) {
            Glide.with(itemView).load(story.photoUrl)
                .into(itemView.findViewById(R.id.iv_post_image))
            itemView.findViewById<TextView>(R.id.tv_username).text = story.name
            itemView.findViewById<TextView>(R.id.tv_description).text = story.description

            itemView.findViewById<TextView>(R.id.tv_posted_at).text =
                if (story.createdAt != null)
                    DateTimeRelative.parseTimeRelative(story.createdAt) else ""

            itemView.setOnClickListener {
                onClick(itemView, story)
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
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}