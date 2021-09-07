package com.example.myapplication.searchRestaurant.model

import androidx.appsearch.annotation.Document

@Document
data class OperatingHours(
    @Document.Namespace
    var namespace : String = "restaurant",
    @Document.Id
    var id : String,
    @Document.StringProperty
    var Friday: String?,
    @Document.StringProperty
    var Monday: String?,
    @Document.StringProperty
    var Saturday: String?,
    @Document.StringProperty
    var Sunday: String?,
    @Document.StringProperty
    var Thursday: String?,
    @Document.StringProperty
    var Tuesday: String?,
    @Document.StringProperty
    var Wednesday: String?
)