package myplayground.example.dicodingstory.activities.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.activities.landing.LandingActivity
import myplayground.example.dicodingstory.components.Theme.ThemeComponent
import myplayground.example.dicodingstory.databinding.ActivitySettingBinding
import myplayground.example.dicodingstory.local_storage.DatastoreSettings
import myplayground.example.dicodingstory.local_storage.dataStore

class SettingActivity : ThemeComponent() {
    private var _binding: ActivitySettingBinding? = null
    private val binding: ActivitySettingBinding
        get() = _binding ?: error("View binding is not initialized")
    private val viewModel: SettingViewModel by viewModels {
        SettingViewModelFactory(DatastoreSettings.getInstance(this.dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivitySettingBinding.inflate(layoutInflater)

        setupAppbar()
        setupContent()
        setupTheme()

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
        val isDarkMode = viewModel.getDarkThemeSettings()
        val isLoggedIn = viewModel.isUserLoggedIn()

        // dark mode switch
        binding.switchDarkMode.isChecked = isDarkMode
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setIsDarkThemeSettings(isChecked)
            recreate()
        }

        // log out button
        if (isLoggedIn) {
            binding.logout.visibility = View.VISIBLE

            binding.logout.setOnClickListener {
                viewModel.logout()

                val intent = Intent(this, LandingActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setupTheme() {
        setTheme(if (viewModel.getDarkThemeSettings()) R.style.AppThemeDark else R.style.AppThemeLight)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}