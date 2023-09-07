package myplayground.example.dicodingstory.utils

import myplayground.example.dicodingstory.model.Story

object DummyData {
    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                i.toString(),
                "title-$i",
                "desc-$i",
                "https://pic.com",
                "",
                0.0F,
                0.0F,
            )
            items.add(story)
        }
        return items
    }
}
