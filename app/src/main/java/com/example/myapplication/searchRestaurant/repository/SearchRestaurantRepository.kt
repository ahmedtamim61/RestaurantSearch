package com.example.myapplication.searchRestaurant.repository

import android.util.Log
import androidx.appsearch.app.GenericDocument
import androidx.appsearch.app.SearchResult
import androidx.appsearch.app.SearchResults
import androidx.appsearch.exceptions.AppSearchException
import com.example.myapplication.searchRestaurant.model.Restaurant
import com.example.myapplication.searchRestaurant.repository.appSearchDB.AppSearchDBHelper
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRestaurantRepository @Inject constructor(private val appSearchDBHelper: AppSearchDBHelper) {

    fun searchRestaurants(query : String, iterate : (page : List<SearchResult>?) -> Unit) {
        val executor = Executors.newSingleThreadExecutor()
        appSearchDBHelper.searchRestaurant(query, object : FutureCallback<SearchResults> {
            override fun onSuccess(searchResults: SearchResults?) {
                iterateSearchResults(searchResults, iterate, executor)
            }

            override fun onFailure(t: Throwable?) {
                Log.e("SearchRes", "Failed to search", t)
            }
        }, executor)
    }

    fun iterateSearchResults(searchResults: SearchResults?, iterate : (page : List<SearchResult>?) -> Unit, executor : Executor) {
        val page = searchResults?.nextPage
        page?.let {
            Futures.transform(page, iterate, executor)
        }
    }

    fun closeSession() = appSearchDBHelper.closeSession()
}