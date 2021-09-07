package com.example.myapplication.searchRestaurant.model

import androidx.appsearch.annotation.Document

@Document
data class LatLng(
    @Document.Namespace
    var namespace : String = "restaurant",
    @Document.Id
    var id : String,
    @Document.DoubleProperty
    var lat: Double?,
    @Document.DoubleProperty
    var lng: Double?
)