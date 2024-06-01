package com.dicoding.asclepius.view.Home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.FragmentHomeBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.Result.ResultActivity
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.lang.StringBuilder
import java.text.NumberFormat
import java.util.UUID

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        with(binding) {
            galleryButton.setOnClickListener { startGallery() }
            analyzeButton.setOnClickListener {
                homeViewModel.currentImageUri.value?.let {
                    analyzeImage(it)
                    Log.d(TAG, it.toString())
                } ?: run {
                    showToast(getString(R.string.empty_image_warning))
                }
            }
        }

        homeViewModel.currentImageUri.observe(viewLifecycleOwner) {uri ->
            uri?.let {
                showImage(it)
            }
        }

        return root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == UCrop.REQUEST_CROP && data != null) {
            val currentImageUri = UCrop.getOutput(data)
            Log.d(TAG, currentImageUri.toString())
            homeViewModel.setCurrentImageUri(currentImageUri!!)
        } else if (resultCode == UCrop.RESULT_ERROR && data != null) {
            val cropError: Throwable? = UCrop.getError(data)
            showToast(cropError.toString())
        }
    }

    private fun startGallery() {
        launcherGallery.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val destinationUri = StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString()
            Log.d(TAG, destinationUri)
            UCrop.of(uri, Uri.fromFile(File(requireContext().cacheDir, destinationUri)))
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(2000, 2000)
                .start(requireContext(), this, UCrop.REQUEST_CROP)

        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage(uri: Uri) {
        Log.d("Image URI", "showImage: $uri")
        binding.previewImageView.setImageURI(uri)
    }

    private fun analyzeImage(uri: Uri) {
        binding.progressIndicator.visibility = View.VISIBLE
        imageClassifierHelper = ImageClassifierHelper(
            context = requireContext(),
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    requireActivity().runOnUiThread() {
                        showToast(error)
                        binding.progressIndicator.visibility = View.GONE
                    }
                }

                override fun onResult(results: List<Classifications>?) {
                    requireActivity().runOnUiThread {
                        results?.let {
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                Log.d(TAG, it.toString())
                                val status = it[0].categories.first().label
                                val percentage = NumberFormat.getPercentInstance()
                                    .format(it[0].categories.first().score)
                                moveToResult(status, percentage)
                            }
                        }
                        binding.progressIndicator.visibility = View.GONE
                    }
                }
            }
        )

        imageClassifierHelper.classifyStaticImage(uri)
    }

    private fun moveToResult(status: String, percentage: String) {
        val intent = Intent(requireContext(), ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, homeViewModel.currentImageUri.value.toString())
        intent.putExtra(ResultActivity.EXTRA_RESULT, status)
        intent.putExtra(ResultActivity.EXTRA_PERCENTAGE, percentage)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "Home"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
