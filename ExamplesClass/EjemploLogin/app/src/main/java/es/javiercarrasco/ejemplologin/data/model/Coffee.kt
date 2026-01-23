package es.javiercarrasco.ejemplologin.data.model

import com.google.gson.annotations.SerializedName

data class Coffee(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("coffee_name")
    var coffeeName: String?,
    @SerializedName("coffee_desc")
    var coffeeDesc: String?,
    @SerializedName("comments")
    var comments: String?
)