package myplayground.example.dicodingstory.activities.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.activities.settings.SettingActivity
import myplayground.example.dicodingstory.components.Theme.ThemeComponent
import myplayground.example.dicodingstory.databinding.ActivityHomeBinding

@SuppressLint("CustomSplashScreen")
class HomeActivity : ThemeComponent() {
    private var _binding: ActivityHomeBinding? = null
    private val binding
        get(): ActivityHomeBinding = _binding ?: error("View binding is not initialized")

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setupAppbar()

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
                    startActivity(intent)
                    true
                }

                else -> {
                    error("not implemented")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}