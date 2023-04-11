package jt.projects.repository.network.model

import com.google.gson.annotations.SerializedName

data class VkUserResponse(
    @SerializedName("photo_max")
    val photoMax: String,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String
)