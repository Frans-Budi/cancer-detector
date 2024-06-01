package com.dicoding.asclepius.view.Result

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.local.HistoryRepository
import com.dicoding.asclepius.data.local.entity.HistoryEntity

class ResultViewModel(application: Application): ViewModel() {
    private val mHistoryRepository: HistoryRepository = HistoryRepository(application)

    fun saveToHistory(his: HistoryEntity) = mHistoryRepository.insert(his)
}