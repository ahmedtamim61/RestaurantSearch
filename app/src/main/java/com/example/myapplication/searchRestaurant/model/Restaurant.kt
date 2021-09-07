package com.example.myapplication.searchRestaurant.model

import androidx.appsearch.annotation.Document
import androidx.appsearch.app.AppSearchSchema

@Document
data class Restaurant(

    @Document.Namespace
    var namespace : String = "restaurant",
    @Document.Id
    var id : String,
    @Document.StringProperty
    var address: String?,
    @Document.StringProperty(indexingType = AppSearchSchema.StringPropertyConfig.INDEXING_TYPE_PREFIXES)
    var cuisineType: String?,
    @Document.DocumentProperty
    var latLng: LatLng?,
    @Document.StringProperty(indexingType = AppSearchSchema.StringPropertyConfig.INDEXING_TYPE_PREFIXES)
    var name: String?,
    @Document.StringProperty
    var neighborhood: String?,
    @Document.DocumentProperty
    var operatingHours: OperatingHours?,
    @Document.StringProperty
    var photograph: String?,
    @Document.DocumentProperty
    var reviews: List<Review?>
) {

    fun toUiModel() : RestaurantUIModel = RestaurantUIModel(name, cuisineType)
}
