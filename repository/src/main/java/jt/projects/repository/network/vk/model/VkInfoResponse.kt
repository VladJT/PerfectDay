package jt.projects.repository.network.vk.model

import com.google.gson.annotations.SerializedName

data class VkInfoResponse(
    @SerializedName("count")
    val count: Long,

    @SerializedName("items")
    val items: List<VkFriendResponse>
)