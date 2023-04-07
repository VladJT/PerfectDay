package jt.projects.repository.network.model

import com.google.gson.annotations.SerializedName

data class VkFriendResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("bdate")
    val bdate: String? = null,

    @SerializedName("photo_200_orig")
    val photo200_Orig: String? = null,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("can_access_closed")
    val canAccessClosed: Boolean,

    @SerializedName("is_closed")
    val isClosed: Boolean,

    @SerializedName("deactivated")
    val deactivated: String? = null
)