package com.dicoding.asclepius.view.History

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.HistoryRepository
import com.dicoding.asclepius.data.local.entity.HistoryEntity

class HistoryViewModel(application: Application): ViewModel() {
    private val mHistoryRepository: HistoryRepository = HistoryRepository(application)

    fun getAllHistories() = mHistoryRepository.getAllHistories()

    private val _histories = MutableLiveData<List<HistoryEntity>>()
    val histories: LiveData<List<HistoryEntity>> = _histories

    private val _textInfo = MutableLiveData<String>()
    val textInfo: LiveData<String> = _textInfo

    init {
        initialGetAllHistoriesData(application)
    }

    private fun initialGetAllHistoriesData(application: Application) {
        _textInfo.value = ""

        getAllHistories().observeForever { histories ->
            if (histories.isNullOrEmpty())
                _textInfo.value = application.getString(R.string.no_histories_data)
            else
                _textInfo.value = ""
                _histories.value = histories
        }
    }
}