package com.dicoding.asclepius.view.Result

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.view.History.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var resultViewModel: ResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resultViewModel = obtainViewModel(this@ResultActivity)

        // Handle AppBar
        binding.toAppBar.setNavigationOnClickListener {
            finish()
        }

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)
        }

        with(binding) {
            val status = intent.getStringExtra(EXTRA_RESULT) ?: ""
            val percentage = intent.getStringExtra(EXTRA_PERCENTAGE) ?: ""
            val photo = intent.getStringExtra(EXTRA_IMAGE_URI) ?: ""
            resultText.text = status
            resultPercentage.text = percentage

            saveToHistory.setOnClickListener {
                val currentTime = System.currentTimeMillis()
                val dateTimeFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                val formattedDateTime = dateTimeFormat.format(Date(currentTime))

                resultViewModel.saveToHistory(
                    HistoryEntity(
                        status = status,
                        percentage = percentage,
                        dateTime = formattedDateTime,
                        photo = photo
                    )
                )

                finish()
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): ResultViewModel {
        val factory = ResultModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[ResultViewModel::class.java]
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_PERCENTAGE = "extra_percentage"
        const val TAG = "ResultActivity"
    }
}