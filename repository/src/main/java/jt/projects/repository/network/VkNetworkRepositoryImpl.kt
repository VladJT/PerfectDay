package jt.projects.repository.network

import jt.projects.model.VkInfo
import jt.projects.repository.network.mapper.toVkInfo
import retrofit2.Retrofit

class VkNetworkRepositoryImpl(retrofit: Retrofit): VkNetworkRepository {
    private val api: VkApi = retrofit.create(VkApi::class.java)

    override suspend fun getUserFriends(userToken: String): VkInfo =
        api.getUserFriends(userToken).toVkInfo()
}