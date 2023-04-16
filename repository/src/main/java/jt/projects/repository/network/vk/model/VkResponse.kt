package jt.projects.repository.network.vk.model

import com.google.gson.annotations.SerializedName

data class VkResponse<T>(
    @SerializedName("response")
    val response: T
)