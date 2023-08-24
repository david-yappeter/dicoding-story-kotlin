package myplayground.example.dicodingstory.activities.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionSet
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.activities.settings.SettingActivity
import myplayground.example.dicodingstory.adapter.StoryListAdapter
import myplayground.example.dicodingstory.components.Theme.ThemeComponent
import myplayground.example.dicodingstory.databinding.ActivityHomeBinding
import myplayground.example.dicodingstory.local_storage.DatastoreSettings
import myplayground.example.dicodingstory.local_storage.dataStore
import myplayground.example.dicodingstory.network.DicodingStoryApi
import myplayground.example.dicodingstory.network.NetworkConfig

class HomeActivity : ThemeComponent() {
    private var _binding: ActivityHomeBinding? = null
    private val binding
        get(): ActivityHomeBinding = _binding ?: error("View binding is not initialized")
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            NetworkConfig.create(
                DicodingStoryApi.BASE_URL,
                DatastoreSettings.getInstance(this.dataStore)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityHomeBinding.inflate(layoutInflater)

        setupAppbar()
        setupContent()

        binding.veilRecyclerView.setAdapter(StoryListAdapter())
        binding.veilRecyclerView.setLayoutManager(LinearLayoutManager(this))
        binding.veilRecyclerView.addVeiledItems(10)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun setupAppbar() {
        val toolbar = binding.appbar.topAppBar

        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.app_menu)

        toolbar.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_settings -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(
                        intent,
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this@HomeActivity,
                        )
                            .toBundle()
                    )
                    true
                }

                else -> {
                    error("not implemented")
                }
            }
        }
    }

    private fun setupContent() {
        viewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
        viewModel.stories.observe(this) { stories ->
            stories.map {
                Log.i("STORYYYY", it.toString())
            }
        }

        viewModel.fetchStories()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}