package jt.projects.repository.network.facts

import com.google.gson.annotations.SerializedName

data class FactResponse(
    @SerializedName("text")
    val text: String
)