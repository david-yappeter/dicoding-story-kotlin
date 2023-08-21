package myplayground.example.dicodingstory.activities.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.components.Theme.ThemeComponent
import myplayground.example.dicodingstory.databinding.ActivitySettingBinding
import myplayground.example.dicodingstory.localstorage.DatastoreSettings
import myplayground.example.dicodingstory.localstorage.dataStore

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
        setupInput()
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

    private fun setupInput() {
        val isDarkMode = viewModel.getDarkThemeSettings()

        binding.switchDarkMode.isChecked = isDarkMode
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setIsDarkThemeSettings(isChecked)
            recreate()
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