package myplayground.example.dicodingstory.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.model.Story

class StoryListAdapter(private val onClickListener: (story: Story) -> Unit = {}) :
    RecyclerView.Adapter<StoryListAdapter.ViewHolder>() {
    private val storyList: MutableList<Story> = mutableListOf()

    override fun getItemCount(): Int = storyList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.story_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(storyList[position])
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(story: Story) {
            Glide.with(itemView).load(story.photoUrl).into(itemView.findViewById(R.id.iv))
            itemView.findViewById<TextView>(R.id.tv_title).text = story.name
            itemView.findViewById<TextView>(R.id.tv_subtitle).text = story.description

            itemView.setOnClickListener {
                onClick(itemView, story)
            }
        }

        private fun onClick(v: View, story: Story) {
            when (v.id) {
                R.id.cl_story -> {
                    onClickListener(story)
                }

                else -> {}
            }
        }
    }
}