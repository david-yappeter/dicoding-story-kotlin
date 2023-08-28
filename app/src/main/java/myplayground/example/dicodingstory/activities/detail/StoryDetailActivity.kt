package myplayground.example.dicodingstory.activities.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.databinding.ActivityStoryDetailBinding
import myplayground.example.dicodingstory.model.Story
import myplayground.example.dicodingstory.util.IntentParcel

class StoryDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_STORY = "extra_story"
    }

    private var _binding: ActivityStoryDetailBinding? = null
    private val binding get() = _binding ?: error("View binding not initialized")
    private val viewModel: StoryDetailViewModel by viewModels {
        StoryDetailViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityStoryDetailBinding.inflate(layoutInflater)

        setupAppbar()
        setupContent()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun setupAppbar() {
        val toolbar = binding.appbar.topAppBar

        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.arrow_back)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupContent() {
        val story = IntentParcel.parseIntent<Story>(intent, EXTRA_STORY)
            ?: error("Intent variable not initialized")

        // view model observer
        viewModel.isLoading.observe(this) { isLoading ->

        }
        viewModel.errorMessage.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
        viewModel.storyDetail.observe(this) { story ->

        }

        Glide.with(this).load(story.photoUrl).into(binding.ivPostImage)
        binding.tvUsername.text = story.name
        binding.tvDescription.text = story.description
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}