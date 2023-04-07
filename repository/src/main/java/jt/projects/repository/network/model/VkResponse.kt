package jt.projects.repository.network.model

import com.google.gson.annotations.SerializedName

data class VkResponse(@SerializedName("response")val response: VkInfoResponse)