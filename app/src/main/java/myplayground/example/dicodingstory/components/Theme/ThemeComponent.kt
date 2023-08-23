package myplayground.example.dicodingstory.components.Theme

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.activities.settings.ThemeComponentViewModel
import myplayground.example.dicodingstory.activities.settings.ThemeComponentViewModelFactory
import myplayground.example.dicodingstory.local_storage.DatastoreSettings
import myplayground.example.dicodingstory.local_storage.dataStore

open class ThemeComponent : AppCompatActivity() {
    private val viewModel: ThemeComponentViewModel by viewModels {
        ThemeComponentViewModelFactory(DatastoreSettings.getInstance(this.dataStore))
    }
    private var isDarkTheme = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setupTheme()

        super.onCreate(savedInstanceState)
    }

    override fun onRestart() {
        if (isDarkTheme != viewModel.getDarkThemeSettings()) {
            recreate()
        }
        super.onRestart()
    }

    fun getDarkThemeSettings(): Boolean {
        return viewModel.getDarkThemeSettings()
    }

    fun setDarkThemeSettings(isDarkTheme: Boolean) {
        return viewModel.setIsDarkThemeSettings(isDarkTheme)
    }

    private fun setupTheme() {
        isDarkTheme = viewModel.getDarkThemeSettings()
        setTheme(if (isDarkTheme) R.style.AppThemeDark else R.style.AppThemeLight)
    }

}