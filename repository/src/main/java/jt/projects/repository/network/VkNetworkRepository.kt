package jt.projects.repository.network

import jt.projects.model.*

interface VkNetworkRepository {
    suspend fun getUserInfo(userToken: String): VkUserInfo

    suspend fun getUserFriends(userToken: String): VkInfo
}