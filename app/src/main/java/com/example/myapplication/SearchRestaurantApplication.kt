package com.example.myapplication

import android.app.Application
import android.content.Context
import androidx.appsearch.localstorage.LocalStorage
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SearchRestaurantApplication : Application()
