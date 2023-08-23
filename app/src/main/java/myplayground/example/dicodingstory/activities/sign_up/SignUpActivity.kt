package myplayground.example.dicodingstory.activities.sign_up

import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.transition.Slide
import android.transition.TransitionSet
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.activities.sign_in.SignInActivity
import myplayground.example.dicodingstory.components.Theme.ThemeComponent
import myplayground.example.dicodingstory.databinding.ActivitySignUpBinding
import myplayground.example.dicodingstory.network.DicodingStoryApi
import myplayground.example.dicodingstory.network.NetworkConfig

class SignUpActivity : ThemeComponent() {
    private var _binding: ActivitySignUpBinding? = null
    private val binding get() = _binding ?: error("View binding is not initialized")
    private val viewModel: SignUpViewModel by viewModels {
        SignUpViewModelFactory(NetworkConfig.create(DicodingStoryApi.BASE_URL))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivitySignUpBinding.inflate(layoutInflater)

        setupAppbar()
        setupContent() //        setupEnterAnimation()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    @Suppress("unused")
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

    private fun setupContent() { // sign up intent
        binding.btnSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK) //            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME)
            startActivity(
                intent, //                ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
            )
            finish()
        }

        // view model obs
        viewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        viewModel.isSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, R.string.registration_success, Toast.LENGTH_LONG).show()

                val intent = Intent(this, SignInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        viewModel.isLoading.observe(this) { isLoading ->
            binding.submitSignUp.isEnabled = !isLoading
            binding.submitSignUpLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // submit sign up
        binding.submitSignUp.setOnClickListener {
            var isValid = true
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (name.isEmpty()) {
                binding.etName.error = this.getString(R.string.error_input_empty)
                isValid = false
            }

            if (email.isEmpty()) {
                binding.etEmail.error = this.getString(R.string.error_input_empty)
                isValid = false
            }

            if (password.isEmpty()) {
                binding.etPassword.error = this.getString(R.string.error_input_empty)
                isValid = false
            }

            if (isValid) {
                viewModel.registerUser(name, email, password)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}