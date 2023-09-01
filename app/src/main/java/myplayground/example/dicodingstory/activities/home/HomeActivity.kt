package myplayground.example.dicodingstory.activities.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.activities.add_story.AddStoryActivity
import myplayground.example.dicodingstory.activities.detail.StoryDetailActivity
import myplayground.example.dicodingstory.activities.maps.MapsActivity
import myplayground.example.dicodingstory.activities.settings.SettingActivity
import myplayground.example.dicodingstory.adapter.LoadingStateAdapter
import myplayground.example.dicodingstory.adapter.StoryListAdapter
import myplayground.example.dicodingstory.components.theme.ThemeComponent
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
                DicodingStoryApi.BASE_URL, DatastoreSettings.getInstance(this.dataStore)
            )
        )
    }
    private val layoutManager = LinearLayoutManager(this)
    private val adapter = StoryListAdapter { v, story ->
        val intent = Intent(v.context, StoryDetailActivity::class.java)
        intent.putExtra(StoryDetailActivity.EXTRA_STORY, story)
        startActivity(
            intent, ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair(v.findViewById(R.id.tv_username), "tv_username"),
                Pair(v.findViewById(R.id.tv_description), "tv_description"),
                Pair(v.findViewById(R.id.iv_post_image), "iv_post_image"),
                Pair(v.findViewById(R.id.iv_post_user), "iv_post_user"),
            ).toBundle()
        )
    }
    private val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AddStoryActivity.INTENT_RESULT_CODE && result.data != null) {
            val isStoryAdded =
                result.data?.getBooleanExtra(AddStoryActivity.EXTRA_IS_STORY_ADDED, false)

            if (isStoryAdded != null && isStoryAdded) {
                adapter.refresh()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityHomeBinding.inflate(layoutInflater)

        setupAppbar()
        setupContent()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun setupAppbar() {
        val toolbar = binding.appbar.topAppBar

        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.map_menu)
        toolbar.inflateMenu(R.menu.app_menu)

        toolbar.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_settings -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(
                        intent,
                    )
                    true
                }

                R.id.action_map -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(
                        intent,
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
        viewModel.pagerStories.observe(this) {
            adapter.submitData(lifecycle, it)
            binding.srl.isRefreshing = false
        }

        // recycler view
        binding.veilRecyclerView.setAdapter(adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        ))
        binding.veilRecyclerView.setLayoutManager(layoutManager)
        binding.veilRecyclerView.addVeiledItems(10)

        // swiper refresh layout
        binding.srl.setOnRefreshListener {
            adapter.refresh()
        }

        // retry fetch
        binding.btnRetry.setOnClickListener {
            adapter.refresh()
        }

        // FAB add
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}