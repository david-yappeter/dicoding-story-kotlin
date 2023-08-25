package myplayground.example.dicodingstory.activities.sign_in

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.activities.sign_up.SignUpActivity
import myplayground.example.dicodingstory.components.Theme.ThemeComponent
import myplayground.example.dicodingstory.databinding.ActivitySignInBinding
import myplayground.example.dicodingstory.local_storage.DatastoreSettings
import myplayground.example.dicodingstory.local_storage.dataStore
import myplayground.example.dicodingstory.network.DicodingStoryApi
import myplayground.example.dicodingstory.network.NetworkConfig

class SignInActivity : ThemeComponent() {
    private var _binding: ActivitySignInBinding? = null
    private val binding get() = _binding ?: error("View binding is not initialized")
    private val viewModel: SignInViewModel by viewModels {
        SignInViewModelFactory(
            NetworkConfig.create(DicodingStoryApi.BASE_URL),
            DatastoreSettings.getInstance(this.dataStore)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivitySignInBinding.inflate(layoutInflater)

        setupAppbar()
        setupContent()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            //            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME)
            startActivity(
                intent,
                //                ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
            )
            finish()
        }

        // view model obs
        viewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        viewModel.isSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                onBackPressedDispatcher.onBackPressed()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.submitSignIn.isEnabled = !isLoading
            binding.submitSignInLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // submit sign up
        binding.submitSignIn.setOnClickListener {
            var isValid = true
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty()) {
                binding.etEmail.error = this.getString(R.string.error_input_empty)
                isValid = false
            }

            if (password.isEmpty()) {
                binding.etPassword.error = this.getString(R.string.error_input_empty)
                isValid = false
            }

            if (isValid) {
                viewModel.loginUser(email, password)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}