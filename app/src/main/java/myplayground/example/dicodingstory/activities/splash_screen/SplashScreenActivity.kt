package myplayground.example.dicodingstory.activities.splash_screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import myplayground.example.dicodingstory.activities.landing.LandingActivity
import myplayground.example.dicodingstory.databinding.ActivitySplashScreenBinding

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding ?: error("View binding is not initialized")

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}