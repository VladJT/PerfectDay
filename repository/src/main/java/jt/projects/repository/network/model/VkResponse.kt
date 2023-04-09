package jt.projects.repository.network.model

import com.google.gson.annotations.SerializedName

data class VkResponse<T>(
    @SerializedName("response")
    val response: T
)