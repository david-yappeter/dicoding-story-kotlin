package myplayground.example.dicodingstory.adapter

import androidx.recyclerview.widget.DiffUtil
import myplayground.example.dicodingstory.model.Story

class StoryDiffCallback(
    private val oldList: List<Story>,
    private val newList: List<Story>,
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}