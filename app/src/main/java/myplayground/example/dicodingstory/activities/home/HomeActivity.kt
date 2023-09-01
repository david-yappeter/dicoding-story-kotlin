package myplayground.example.dicodingstory.activities.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.activities.add_story.AddStoryActivity
import myplayground.example.dicodingstory.activities.detail.StoryDetailActivity
import myplayground.example.dicodingstory.activities.maps.MapsActivity
import myplayground.example.dicodingstory.activities.settings.SettingActivity
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
                viewModel.currentPage.value = 1
                viewModel.fetchStories()
                layoutManager.scrollToPosition(0)
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
        viewModel.currentPage.value = 1 // view model observer
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.errorContainer.visibility = View.GONE
                binding.veilRecyclerView.visibility = View.VISIBLE
                binding.veilRecyclerView.veil()
            } else {
                binding.veilRecyclerView.unVeil()
                binding.srl.isRefreshing = false
            }
        }
        viewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            binding.errorContainer.visibility = View.VISIBLE
            binding.veilRecyclerView.visibility = View.GONE
        }
        viewModel.stories.observe(this) { stories ->
            stories.map {
                adapter.replaceData(stories)
            }
        }
        viewModel.appendStories.observe(this) { stories ->
            stories.map {
                adapter.addData(stories)
            }
        }
        viewModel.pagerStories.observe(this) {
            adapter.submitData(lifecycle, it)
        }

        // recycler view
        binding.veilRecyclerView.setAdapter(adapter)
        binding.veilRecyclerView.setLayoutManager(layoutManager)
        binding.veilRecyclerView.addVeiledItems(10)

//        binding.veilRecyclerView.getRecyclerView()
//            .addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    val isLoading = viewModel.isLoading.value as Boolean
//                    val isLastPage = viewModel.isLastPage.value as Boolean
//                    super.onScrolled(recyclerView, dx, dy)
//
//                    // Load more data when the user is near the end of the list
//                    if (!isLoading && !isLastPage) {
//                        val visibleItemCount = layoutManager.childCount
//                        val totalItemCount = layoutManager.itemCount
//                        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//
//                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
//                            viewModel.fetchStories(true)
//                        }
//                    }
//                }
//            })

        // swiper refresh layout
        binding.srl.setOnRefreshListener {
            if (viewModel.isLoading.value != null && viewModel.isLoading.value == false) {
                viewModel.currentPage.value = 1
                viewModel.fetchStories()
                layoutManager.scrollToPosition(0)
            }
        }

        // retry fetch
        binding.btnRetry.setOnClickListener {
            viewModel.fetchStories()
        }

        // FAB add
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            resultLauncher.launch(intent)
        }

        // fetch stories
        viewModel.fetchStories()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}