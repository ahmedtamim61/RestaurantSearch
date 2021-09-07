package com.example.myapplication.searchRestaurant.repository.appSearchDB

import android.content.Context
import android.util.Log
import androidx.appsearch.app.*
import com.example.myapplication.R
import com.example.myapplication.searchRestaurant.model.*
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

const val NAMESPACE_RESTAURANT = "restaurant"
const val NAMESPACE_LOCATION = "location"
const val NAMESPACE_OPERATION_HOURS = "opHours"
const val NAMESPACE_REVIEW = "review"
const val NAMESPACE_MENU = "menu"
const val NAMESPACE_CATEGORY = "category"
const val NAMESPACE_MENU_ITEM = "menuItem"

@Singleton
class AppSearchDBHelper @Inject constructor(@ApplicationContext private val context : Context,
                                            private val sessionFuture: ListenableFuture<AppSearchSession>) {

    init {
        val executor = Executors.newSingleThreadExecutor()
        val setSchemaFuture = setSchema(executor)
        addRestaurants(executor, setSchemaFuture)
        addMenus(executor, setSchemaFuture)
        persistDocuments(executor)
    }

    private fun addRestaurants(executor: Executor, setSchemaFuture: ListenableFuture<SetSchemaResponse>) {
        val objectArrayString: String = context.resources.openRawResource(R.raw.restaurants).bufferedReader().use { it.readText() }
        val jsonArray = Gson().fromJson(objectArrayString, JsonObject::class.java).getAsJsonArray("restaurants")
        val docs = mutableListOf<Any>()
        jsonArray.forEach {
            val restaurant = Gson().fromJson(it, Restaurant::class.java)
            restaurant.apply {
                namespace = NAMESPACE_RESTAURANT
                latLng?.namespace = NAMESPACE_LOCATION
                latLng?.id = id
                operatingHours?.namespace = NAMESPACE_OPERATION_HOURS
                operatingHours?.id = id
                var temp = 0
                reviews.forEach { review ->
                    review?.namespace = NAMESPACE_REVIEW
                    review?.id = (id.toInt() + temp++).toString()
                }
            }
            docs.add(restaurant)
        }
        setSchemaFuture.addListener({
            addDocument(docs, executor)
        }, executor)
    }

    private fun addMenus(executor: Executor, setSchemaFuture: ListenableFuture<SetSchemaResponse>) {
        val objectArrayString: String = context.resources.openRawResource(R.raw.menus).bufferedReader().use { it.readText() }
        val menusJsonArray = Gson().fromJson(objectArrayString, JsonObject::class.java).getAsJsonArray("menus")
        val docs = mutableListOf<Any>()
        var temp = 0
        menusJsonArray.forEach {
            val menu = Gson().fromJson(it, Menu::class.java)
            menu.apply {
                namespace = NAMESPACE_MENU
                id = (temp++).toString()
                categories.forEach { category ->
                    category?.namespace = NAMESPACE_CATEGORY
                    category?.menuItems?.forEach { item ->
                        item?.namespace = NAMESPACE_MENU_ITEM
                    }
                }
            }
            docs.add(menu)
        }
        setSchemaFuture.addListener({
            addDocument(docs, executor)
        }, executor)
    }

    private fun persistDocuments(executor: Executor) {
        val requestFlushFuture = Futures.transformAsync(sessionFuture,
            { session -> session?.requestFlush() }, executor)

        Futures.addCallback(requestFlushFuture, object : FutureCallback<Void?> {
            override fun onSuccess(result: Void?) {
                Log.e("SearchRes", "Flush database updates success.")
            }

            override fun onFailure(t: Throwable) {
                Log.e("SearchRes", "Failed to flush database updates.", t)
            }
        }, executor)
    }

    private fun setSchema(executor: Executor) = Futures.transformAsync(sessionFuture, { session ->
        session?.setSchema(SetSchemaRequest.Builder().addDocumentClasses(
            Category::class.java, Menu::class.java, MenuItem::class.java, Restaurant::class.java,
            LatLng::class.java, OperatingHours::class.java, Review::class.java).build())
        }, executor)


    private fun addDocument(documents: List<Any>, executor: Executor) {

        val putRequest = PutDocumentsRequest.Builder().addDocuments(documents).build()
        val putFuture = Futures.transformAsync(sessionFuture, { session -> session?.put(putRequest) }, executor)

        Futures.addCallback(putFuture, object : FutureCallback<AppSearchBatchResult<String, Void>?> {
                override fun onSuccess(result: AppSearchBatchResult<String, Void>?) {
                    val successfulResults = result?.successes
                    Log.e("SearchRes", "${successfulResults?.size} documents added")
                }

                override fun onFailure(t: Throwable) {
                    Log.e("SearchRes", "Failed to put documents.", t)
                }
            }, executor)
    }

    fun searchRestaurant(query : String, callback: FutureCallback<SearchResults>, executor : Executor) {

        val searchSpec = SearchSpec.Builder().build()
        val searchFuture = Futures.transform(sessionFuture, {
                session -> session?.search(query, searchSpec)
            }, executor)
        Futures.addCallback(searchFuture, callback, executor)
    }

    fun closeSession() {
        Futures.transform(sessionFuture, {
            it?.close()
        }, Executors.newSingleThreadExecutor())
    }
}