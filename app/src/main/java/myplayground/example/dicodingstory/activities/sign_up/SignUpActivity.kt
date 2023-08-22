package myplayground.example.dicodingstory.activities.sign_up

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Explode
import android.transition.Slide
import android.transition.TransitionSet
import android.view.Gravity
import android.view.Window
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.activities.sign_in.SignInActivity
import myplayground.example.dicodingstory.components.Theme.ThemeComponent
import myplayground.example.dicodingstory.databinding.ActivitySignUpBinding

class SignUpActivity : ThemeComponent() {
    private var _binding: ActivitySignUpBinding? = null
    private val binding get() = _binding ?: error("View binding is not initialized")

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivitySignUpBinding.inflate(layoutInflater)

        setupAppbar()
        setupContent()
//        setupEnterAnimation()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun setupEnterAnimation() {
        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

            val slide = Slide()
            slide.slideEdge = Gravity.END
            slide.duration = 300

            enterTransition = TransitionSet().apply {
                addTransition(slide)
            }

            exitTransition = Explode()
        }
    }

    private fun setupAppbar() {
        val toolbar = binding.appbar.topAppBar

        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.arrow_back)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupContent() {
        // sign up intent
        binding.btnSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            //            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME)
            startActivity(
                intent,
//                ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
            )
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}