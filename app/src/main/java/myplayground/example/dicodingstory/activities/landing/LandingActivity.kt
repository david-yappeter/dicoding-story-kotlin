package myplayground.example.dicodingstory.activities.landing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.app.ActivityOptionsCompat
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.activities.settings.SettingActivity
import myplayground.example.dicodingstory.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {
    private var _binding: ActivityLandingBinding? = null
    private val binding get() = _binding ?: error("View binding not initialized")

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityLandingBinding.inflate(layoutInflater)

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
                    startActivity(
                        intent,
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this@LandingActivity,
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}