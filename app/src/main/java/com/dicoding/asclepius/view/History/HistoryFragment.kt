package com.dicoding.asclepius.view.History

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.view.adapter.ArticleItemAdapter
import com.dicoding.asclepius.view.adapter.HistoryItemAdapter

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private lateinit var historyViewModel: HistoryViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        historyViewModel = obtainViewModel()

        historyViewModel.histories.observe(viewLifecycleOwner) { histories ->
            setHistoryItemsData(histories)
        }

        historyViewModel.textInfo.observe(viewLifecycleOwner) {textInfo ->
            binding.textInfo.text = textInfo
        }

        with(binding) {
            // Perform Optimize Recycle View
            rvHistory.setHasFixedSize(true)

            // Initial for Recycle View Layout
            val layoutManager = LinearLayoutManager(requireContext())
            rvHistory.layoutManager = layoutManager
            // Initial for Adapter List Items
            val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
            rvHistory.addItemDecoration(itemDecoration)
        }

        return root
    }

    private fun setHistoryItemsData(histories: List<HistoryEntity>?) {
        val adapter = HistoryItemAdapter(requireContext())
        adapter.submitList(histories)
        binding.rvHistory.adapter = adapter
    }

    private fun obtainViewModel(): HistoryViewModel {
        val factory = HistoryModelFactory.getInstance(requireActivity().application)
        return ViewModelProvider(this, factory)[HistoryViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}