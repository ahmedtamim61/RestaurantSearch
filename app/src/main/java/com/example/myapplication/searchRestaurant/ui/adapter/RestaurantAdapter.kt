package com.example.myapplication.searchRestaurant.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ListItemRestaurantBinding
import com.example.myapplication.searchRestaurant.model.RestaurantUIModel

class RestaurantAdapter(private var items : List<RestaurantUIModel?>)
    : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    fun updateData(items : List<RestaurantUIModel?>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RestaurantViewHolder {
        val binding = ListItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class RestaurantViewHolder(private val binding : ListItemRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(restaurantUIModel: RestaurantUIModel?) {
            binding.restaurantModel = restaurantUIModel
            binding.executePendingBindings()
        }
    }
}