package myplayground.example.dicodingstory.activities.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.databinding.ActivityStoryDetailBinding
import myplayground.example.dicodingstory.network.DicodingStoryApi
import myplayground.example.dicodingstory.network.NetworkConfig

class StoryDetailActivity : AppCompatActivity() {
    private var _binding: ActivityStoryDetailBinding? = null
    private val binding get() = _binding ?: error("View binding not initialized")
    private val viewModel: StoryDetailViewModel by viewModels {
        StoryDetailViewModelFactory(NetworkConfig.create(DicodingStoryApi.BASE_URL))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityStoryDetailBinding.inflate(layoutInflater)

        setupContent()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun setupContent() {
        // view model observer
        viewModel.isLoading.observe(this) {isLoading ->

        }
        viewModel.errorMessage.observe(this) {errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
        viewModel.storyDetail.observe(this) {story ->

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}