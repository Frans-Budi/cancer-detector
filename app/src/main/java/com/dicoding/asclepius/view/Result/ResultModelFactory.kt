package com.dicoding.asclepius.view.Result

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ResultModelFactory private constructor(
    private val mApplication: Application
): ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java))
            return ResultViewModel(mApplication) as T

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: ResultModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ResultModelFactory {
            if (INSTANCE == null) {
                synchronized(ResultModelFactory::class.java) {
                    INSTANCE = ResultModelFactory(application)
                }
            }
            return INSTANCE as ResultModelFactory
        }
    }
}