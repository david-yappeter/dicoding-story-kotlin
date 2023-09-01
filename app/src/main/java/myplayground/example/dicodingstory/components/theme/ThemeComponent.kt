package myplayground.example.dicodingstory.components.theme

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import myplayground.example.dicodingstory.local_storage.DatastoreSettings
import myplayground.example.dicodingstory.local_storage.dataStore

open class ThemeComponent : AppCompatActivity() {
    private val viewModel: ThemeComponentViewModel by viewModels {
        ThemeComponentViewModelFactory(DatastoreSettings.getInstance(this.dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupTheme()

        super.onCreate(savedInstanceState)
    }

    private fun setupTheme() {
        viewModel.getDarkThemeSettingsAsync().observe(this) { isDarkTheme ->
            if (isDarkTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}