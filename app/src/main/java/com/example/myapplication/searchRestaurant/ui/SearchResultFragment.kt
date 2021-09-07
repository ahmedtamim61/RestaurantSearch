package com.example.myapplication.searchRestaurant.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSearchResultBinding
import com.example.myapplication.databinding.ListItemRestaurantBinding
import com.example.myapplication.searchRestaurant.ui.adapter.RestaurantAdapter
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.OnBackPressedCallback

@AndroidEntryPoint
class SearchResultFragment : Fragment() {

    private val viewModel : SearchRestaurantViewModel by viewModels()
    private lateinit var binding : FragmentSearchResultBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchResultBinding.inflate(LayoutInflater.from(requireContext()), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.searchView.doAfterTextChanged { text ->
            if(!text.isNullOrEmpty())
                viewModel.searchRestaurant(text.toString())
        }
        viewModel.restaurantsLiveData.observe(viewLifecycleOwner, {
            it?.let { list ->
                binding.recyclerView.adapter?.let { adapter ->
                    (adapter as RestaurantAdapter).updateData(list)
                } ?: kotlin.run {
                    binding.recyclerView.adapter = RestaurantAdapter(list)
                }
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }
}