package com.example.myapplication.searchRestaurant.ui

import android.util.Log
import androidx.appsearch.app.GenericDocument
import androidx.appsearch.app.SearchResult
import androidx.appsearch.exceptions.AppSearchException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.searchRestaurant.model.Menu
import com.example.myapplication.searchRestaurant.model.MenuItem
import com.example.myapplication.searchRestaurant.model.Restaurant
import com.example.myapplication.searchRestaurant.model.RestaurantUIModel
import com.example.myapplication.searchRestaurant.repository.SearchRestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchRestaurantViewModel @Inject constructor(private val repository: SearchRestaurantRepository) : ViewModel() {

    val restaurantsLiveData = MutableLiveData<List<RestaurantUIModel?>>()

    fun searchRestaurant(query : String) {
        val searchResultList : MutableList<RestaurantUIModel?> = mutableListOf()
        repository.searchRestaurants(query) { page: List<SearchResult>? ->
            Log.e("SearchRes", "${page?.size} found")
            page?.let {
                page.forEach {
                    val genericDocument: GenericDocument = it.genericDocument
                    val schemaType = genericDocument.schemaType
                    try {
                        if (schemaType == Restaurant::class.simpleName) {
                            searchResultList.add(genericDocument.toDocumentClass(Restaurant::class.java).toUiModel())
                        } else if (schemaType == MenuItem::class.simpleName) {
                            val item = genericDocument.toDocumentClass(MenuItem::class.java)
                            searchResultList.add(RestaurantUIModel(item.name))
                        }
                    } catch (e: AppSearchException) {
                        Log.e("SearchRes", "Failed to convert GenericDocument", e)
                        null
                    }
                }
            }
        }
        restaurantsLiveData.value = searchResultList
    }

    override fun onCleared() {
        super.onCleared()
        repository.closeSession()
    }
}