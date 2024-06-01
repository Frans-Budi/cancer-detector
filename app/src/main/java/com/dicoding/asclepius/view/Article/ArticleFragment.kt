package com.dicoding.asclepius.view.Article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.FragmentArticleBinding
import com.dicoding.asclepius.view.adapter.ArticleItemAdapter

class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null
    private lateinit var viewModel: ArticleViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        viewModel.listArticles.observe(viewLifecycleOwner) { articles ->
            setArticlesItemsData(articles)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { isLoading ->
                showLoading(isLoading)
            }
        }
        viewModel.toast.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                showToast(message)
            }
        }

        with(binding) {
            // Perform Optimize Recycle View
            rvArticles.setHasFixedSize(true)

            // Initial for Recycle View Layout
            val layoutManager = LinearLayoutManager(requireContext())
            rvArticles.layoutManager = layoutManager
            // Initial for Adapter List Items
            val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
            rvArticles.addItemDecoration(itemDecoration)
        }

        return root
    }

    private fun setArticlesItemsData(articles: List<ArticlesItem>?) {
        val adapter = ArticleItemAdapter(requireContext())
        adapter.submitList(articles)
        binding.rvArticles.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}