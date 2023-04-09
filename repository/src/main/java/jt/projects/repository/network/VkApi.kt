package jt.projects.repository.network

import jt.projects.repository.BuildConfig
import jt.projects.repository.network.model.VkResponse
import retrofit2.http.*

private const val ORDER_BY = "hints"
private const val FIELDS = "bdate, photo_200_orig"
private const val COUNT = 150

interface VkApi {

    @GET("method/friends.get")
    suspend fun getUserFriends(
        @Query("access_token") userToken: String,
        @Query("order") order: String = ORDER_BY,
        @Query("fields") fields: String = FIELDS,
        @Query("count") count: Int = COUNT,
        @Query("v") version: Double = BuildConfig.VK_VERSION_API
    ): VkResponse
}