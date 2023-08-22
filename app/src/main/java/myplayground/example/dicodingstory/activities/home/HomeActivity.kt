package myplayground.example.dicodingstory.activities.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionSet
import android.view.Gravity
import android.view.MenuItem
import android.view.Window
import androidx.core.app.ActivityOptionsCompat
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.activities.settings.SettingActivity
import myplayground.example.dicodingstory.components.Theme.ThemeComponent
import myplayground.example.dicodingstory.databinding.ActivityHomeBinding

class HomeActivity : ThemeComponent() {
    private var _binding: ActivityHomeBinding? = null
    private val binding
        get(): ActivityHomeBinding = _binding ?: error("View binding is not initialized")

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityHomeBinding.inflate(layoutInflater)

        setupAppbar()
        setupTransition()

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

    private fun setupTransition() {
        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

            val slide = Slide()
            slide.slideEdge = Gravity.START
            slide.duration = 300

            exitTransition = TransitionSet().apply {
                addTransition(slide)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}