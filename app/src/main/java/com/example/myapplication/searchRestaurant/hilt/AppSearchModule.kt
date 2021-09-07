package com.example.myapplication.searchRestaurant.hilt

import android.content.Context
import androidx.appsearch.app.AppSearchSession
import androidx.appsearch.localstorage.LocalStorage
import com.google.common.util.concurrent.ListenableFuture
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppSearchModule {

    @Provides
    @Singleton
    fun provideAppSearchSession(@ApplicationContext appContext: Context) : ListenableFuture<AppSearchSession> =
        LocalStorage.createSearchSession(LocalStorage.SearchContext.Builder(appContext, "restaurants_search")
            .build())
}