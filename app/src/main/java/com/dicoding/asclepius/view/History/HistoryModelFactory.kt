package com.dicoding.asclepius.view.History

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HistoryModelFactory private constructor(
    private val mApplication: Application
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java))
            return HistoryViewModel(mApplication) as T

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: HistoryModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): HistoryModelFactory {
            if (INSTANCE == null) {
                synchronized(HistoryModelFactory::class.java) {
                    INSTANCE = HistoryModelFactory(application)
                }
            }
            return INSTANCE as HistoryModelFactory
        }
    }
}