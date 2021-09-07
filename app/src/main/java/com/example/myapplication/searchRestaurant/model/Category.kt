package com.example.myapplication.searchRestaurant.model

import androidx.appsearch.annotation.Document
import androidx.appsearch.app.AppSearchSchema

@Document
data class Category(
    @Document.Namespace
    var namespace : String,
    @Document.Id
    var id : String,
    @Document.DocumentProperty
    var menuItems: List<MenuItem?>,
    @Document.StringProperty(indexingType = AppSearchSchema.StringPropertyConfig.INDEXING_TYPE_PREFIXES)
    var name: String?
)