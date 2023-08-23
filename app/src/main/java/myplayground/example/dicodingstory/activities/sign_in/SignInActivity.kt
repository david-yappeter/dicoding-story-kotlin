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
import myplayground.example.dicodingstory.network.DicodingStoryApi
import myplayground.example.dicodingstory.network.NetworkConfig

class SignInActivity : ThemeComponent() {
    private var _binding: ActivitySignInBinding? = null
    private val binding get() = _binding ?: error("View binding is not initialized")
    private val viewModel: SignInViewModel by viewModels {
        SignInViewModelFactory(NetworkConfig.create(DicodingStoryApi.BASE_URL))
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
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}