package com.example.myapplication.searchRestaurant.model

import androidx.appsearch.annotation.Document
import androidx.appsearch.app.AppSearchSchema

@Document
data class MenuItem(
    @Document.Namespace
    var namespace : String,
    @Document.Id
    var id : String,
    @Document.StringProperty
    var description: String?,
    @Document.StringProperty(indexingType = AppSearchSchema.StringPropertyConfig.INDEXING_TYPE_PREFIXES)
    var name: String?,
    @Document.StringProperty
    var price: String?
)