package myplayground.example.dicodingstory.activities.landing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.activities.home.HomeActivity
import myplayground.example.dicodingstory.activities.settings.SettingActivity
import myplayground.example.dicodingstory.activities.sign_in.SignInActivity
import myplayground.example.dicodingstory.activities.sign_up.SignUpActivity
import myplayground.example.dicodingstory.components.Theme.ThemeComponent
import myplayground.example.dicodingstory.databinding.ActivityLandingBinding
import myplayground.example.dicodingstory.local_storage.DatastoreSettings
import myplayground.example.dicodingstory.local_storage.dataStore

class LandingActivity : ThemeComponent() {
    private var _binding: ActivityLandingBinding? = null
    private val binding get() = _binding ?: error("View binding not initialized")
    private val viewModel: LandingViewModel by viewModels {
        LandingViewModelFactory(DatastoreSettings.getInstance(this.dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityLandingBinding.inflate(layoutInflater)

        setupAppbar()
        setupContent()

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

    private fun setupContent() {
        // Sign In
        val signInBtn = binding.btnSignIn

        signInBtn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(
                intent,
                //                ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
            )
        }

        // Sign Up
        val signUpBtn = binding.btnSignUp

        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(
                intent,
                //                ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
            )
        }

        viewModel.getUserData().observe(this) { userData ->
            Log.i("USERDATA", userData.toString())
            if (userData != null) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}