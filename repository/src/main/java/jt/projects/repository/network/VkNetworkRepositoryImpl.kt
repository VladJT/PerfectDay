package jt.projects.repository.network

import jt.projects.model.VkInfo

class VkNetworkRepositoryImpl : VkNetworkRepository {
    override suspend fun getUserFriends(userToken: String): VkInfo {
        TODO("Not yet implemented")
    }
}