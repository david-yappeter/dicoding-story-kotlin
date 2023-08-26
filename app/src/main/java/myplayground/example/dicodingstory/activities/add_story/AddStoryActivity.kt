package myplayground.example.dicodingstory.activities.add_story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.databinding.ActivityAddStoryBinding

class AddStoryActivity : AppCompatActivity() {
    private var _binding: ActivityAddStoryBinding? = null
    private val binding get() = _binding ?: error("View binding not initialized")

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityAddStoryBinding.inflate(layoutInflater)

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

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}