package com.dicoding.asclepius.view.Article

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.remote.response.ArticleResponse
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig
import com.dicoding.asclepius.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleViewModel: ViewModel() {

    private val _listArticles = MutableLiveData<List<ArticlesItem>>()
    val listArticles: LiveData<List<ArticlesItem>> = _listArticles

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    private val _toast = MutableLiveData<Event<String>>()
    val toast: LiveData<Event<String>> = _toast

    init {
        getArticles()
    }

    fun getArticles() {
        _isLoading.value = Event(true)
        val client = ApiConfig.getApiService().getArticles(
            query = "cancer",
            category = "health",
            language = "en",
            apiKey = BuildConfig.API_KEY
        )
        client.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(
                call: Call<ArticleResponse>,
                response: Response<ArticleResponse>
            ) {
                _isLoading.value = Event(false)

                if (response.isSuccessful) {
                    val articles = response.body()?.articles
                    if (articles != null) {
                        _listArticles.value = articles.filterNotNull()
                    }
                } else {
                    Log.d(TAG, "onResponse: ${response.message()}")
                    _toast.value = Event(response.message())
                }
            }

            override fun onFailure(
                call: Call<ArticleResponse>,
                t: Throwable
            ) {
                _isLoading.value = Event(false)
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _toast.value = Event(t.message.toString())
            }
        })
    }

    companion object {
        private const val TAG = "ArticleViewModel"
    }
}