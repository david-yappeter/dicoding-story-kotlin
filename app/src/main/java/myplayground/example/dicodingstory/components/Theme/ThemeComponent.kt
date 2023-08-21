package myplayground.example.dicodingstory.components.Theme

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.activities.settings.ThemeComponentViewModel
import myplayground.example.dicodingstory.activities.settings.ThemeComponentViewModelFactory
import myplayground.example.dicodingstory.localstorage.DatastoreSettings
import myplayground.example.dicodingstory.localstorage.dataStore

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