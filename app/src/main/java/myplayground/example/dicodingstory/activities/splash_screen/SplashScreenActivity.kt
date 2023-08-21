package myplayground.example.dicodingstory.activities.splash_screen

import androidx.lifecycle.asLiveData
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.databinding.ActivitySplashScreenBinding
import myplayground.example.dicodingstory.localstorage.DatastoreSettings
import myplayground.example.dicodingstory.localstorage.LocalStorageManager
import myplayground.example.dicodingstory.localstorage.SharedPreferencesSettings
import myplayground.example.dicodingstory.localstorage.dataStore

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private var _binding: ActivitySplashScreenBinding? = null
    private val binding
        get(): ActivitySplashScreenBinding = _binding ?: error("View binding is not initialized")
    private lateinit var localStorageManager: LocalStorageManager

    private var isDarkTheme = false

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        localStorageManager = DatastoreSettings.getInstance(application.dataStore)

        setupTheme()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun setupTheme() {
        runBlocking {
            isDarkTheme = localStorageManager.getDarkThemePreference()
            setTheme(if (isDarkTheme) R.style.AppThemeDark else R.style.AppThemeLight)
        }

        binding.btn.setOnClickListener {
            runBlocking {
                launch {
                    localStorageManager.saveDarkThemePreference(!isDarkTheme)
                }
            }

            recreate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}