package jt.projects.repository.network.vk

import jt.projects.model.*
import jt.projects.repository.network.mapper.*
import retrofit2.Retrofit

class VkNetworkRepositoryImpl(retrofit: Retrofit): VkNetworkRepository {
    private val api: VkApi = retrofit.create(VkApi::class.java)

    override suspend fun getUserInfo(userToken: String): VkUserInfo  =
        api.getUserInfo(userToken).response.firstOrNull()?.toVkUserInfo() ?: VkUserInfo.EMPTY

    override suspend fun getUserFriends(userToken: String): VkInfo =
        api.getUserFriends(userToken).response.toVkInfo()
}