package com.example.myapplication.searchRestaurant.model

import androidx.appsearch.annotation.Document

@Document
data class Menu(
    @Document.Namespace
    var namespace : String,
    @Document.Id
    var id : String,
    @Document.DocumentProperty
    var categories: List<Category?>,
    @Document.LongProperty
    var restaurantId: Long?
)