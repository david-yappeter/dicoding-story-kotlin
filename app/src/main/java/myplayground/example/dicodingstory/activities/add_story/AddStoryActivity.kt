package myplayground.example.dicodingstory.activities.add_story

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import myplayground.example.dicodingstory.R
import myplayground.example.dicodingstory.components.theme.ThemeComponent
import myplayground.example.dicodingstory.databinding.ActivityAddStoryBinding
import myplayground.example.dicodingstory.local_storage.DatastoreSettings
import myplayground.example.dicodingstory.local_storage.dataStore
import myplayground.example.dicodingstory.network.DicodingStoryApi
import myplayground.example.dicodingstory.network.NetworkConfig
import myplayground.example.dicodingstory.util.FileUtils
import myplayground.example.dicodingstory.util.openAppSystemSettings
import java.io.File

class AddStoryActivity : ThemeComponent() {
    companion object {
        const val PERMISSION_REQUEST_CODE = 10
        const val INTENT_RESULT_CODE = 1
        const val EXTRA_IS_STORY_ADDED = "is_story_added"
    }

    private lateinit var currentPhotoPath: String
    private var _binding: ActivityAddStoryBinding? = null
    private val binding get() = _binding ?: error("View binding not initialized")
    private val viewModel: AddStoryViewModel by viewModels {
        AddStoryViewModelFactory(
            NetworkConfig.create(

                DicodingStoryApi.BASE_URL, DatastoreSettings.getInstance(this.dataStore)
            ), getSystemService(Context.LOCATION_SERVICE) as LocationManager
        )
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = FileUtils.uriToFile(uri, this@AddStoryActivity)
                viewModel.imageFile.value = myFile
                viewModel.galleryUri.value = uri
            }
        }
    }
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                val bitmapImage = BitmapFactory.decodeFile(file.path)
                viewModel.cameraBitmap.value = bitmapImage
                viewModel.imageFile.value = file
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityAddStoryBinding.inflate(layoutInflater)

        setupAppbar()
        setupContent()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupPermission()
    }

    private fun setupAppbar() {
        val toolbar = binding.appbar.topAppBar

        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.arrow_back)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupContent() { // view model observer
        viewModel.cameraBitmap.observe(this) { bitmap ->
            if (bitmap != null) {
                binding.ivPostImage.setImageBitmap(bitmap)
            }
        }
        viewModel.galleryUri.observe(this) { uri ->
            if (uri != null) {
                binding.ivPostImage.setImageURI(uri)
            }
        }
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.btnUploadLoading.visibility = View.VISIBLE
                binding.btnUpload.isEnabled = false
                binding.btnCamera.isEnabled = false
                binding.btnGallery.isEnabled = false
            } else {
                binding.btnUploadLoading.visibility = View.GONE
                binding.btnUpload.isEnabled = true
                binding.btnCamera.isEnabled = true
                binding.btnGallery.isEnabled = true
            }
        }
        viewModel.errorMessage.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
        viewModel.isSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Story added", Toast.LENGTH_SHORT).show()
                val resultIntent = intent
                resultIntent.putExtra(EXTRA_IS_STORY_ADDED, true)
                setResult(INTENT_RESULT_CODE, resultIntent)
                onBackPressedDispatcher.onBackPressed()
            }
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }
        binding.btnCamera.setOnClickListener {
            startCamera()
        }
        binding.btnUpload.setOnClickListener {
            val description = binding.etDesc.text.toString()

            if (description.isEmpty()) {
                binding.etDesc.error = this.getString(R.string.error_input_empty)
            } else {
                viewModel.addStory(description)
            }
        }
        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->
            viewModel.isIncludeLocation.value = isChecked
        }
    }

    private fun setupPermission() {
        val cameraPermission = Manifest.permission.CAMERA
        //        val galleryPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        val requiredPermissions = arrayOf(
            cameraPermission,
            //            galleryPermission,
            locationPermission
        )

        val allPermissionGranted = requiredPermissions.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }

        if (!allPermissionGranted) {
            ActivityCompat.requestPermissions(
                this,
                requiredPermissions,
                PERMISSION_REQUEST_CODE,
            )
        }

        val cameraPermissionGranted = ContextCompat.checkSelfPermission(
            this, cameraPermission
        ) == PackageManager.PERMISSION_GRANTED
        val locationPermissionGranted = ContextCompat.checkSelfPermission(
            this, locationPermission
        ) == PackageManager.PERMISSION_GRANTED

        val permissionsToRequest = mutableListOf<String>()

        if (!cameraPermissionGranted) {
            permissionsToRequest.add(cameraPermission)
        }

        if (!locationPermissionGranted) {
            permissionsToRequest.add(locationPermission)
        }

        if (permissionsToRequest.isNotEmpty()) { // Request the permissions that are not granted.
            ActivityCompat.requestPermissions(
                this, permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun showExplanation(
        title: String,
        onPositiveButton: () -> Unit = {},
    ) {
        val message = "Denied for the 2nd time, Must be manual set from settings"
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title).setMessage(message).setPositiveButton(
            android.R.string.ok
        ) { _, _ ->
            onPositiveButton()
        }
        builder.create().show()
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)


        FileUtils.createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity, "myplayground.example.dicodingstory", it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (i in permissions.indices) {
                val permission = permissions[i]
                val grantResult = grantResults[i]

                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    val deniedPermissions = listOf<String>().toMutableList()
                    when (permission) {
                        Manifest.permission.CAMERA -> {
                            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                    this, Manifest.permission.CAMERA
                                )
                            ) {
                                deniedPermissions.add("Camera")
                            }
                        }

                        Manifest.permission.ACCESS_FINE_LOCATION -> {
                            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                    this, Manifest.permission.ACCESS_FINE_LOCATION
                                )
                            ) {
                                deniedPermissions.add("Location")
                            }
                        }
                    }

                    if (deniedPermissions.isNotEmpty()) {
                        val title = deniedPermissions.joinToString(separator = ",")
                        showExplanation(
                            "$title Permission denied",
                        ) {
                            applicationContext.openAppSystemSettings()
                        }

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}