package jt.projects.repository.network

import jt.projects.model.VkInfo

interface VkNetworkRepository {
    suspend fun getUserFriends(userToken: String): VkInfo
}