package com.example.myapplication.searchRestaurant.model

import androidx.appsearch.annotation.Document

@Document
data class Review(
    @Document.Namespace
    var namespace : String = "restaurant",
    @Document.Id
    var id : String,
    @Document.StringProperty
    var comments: String?,
    @Document.StringProperty
    var date: String?,
    @Document.StringProperty
    var name: String?,
    @Document.LongProperty
    var rating: Long?
)