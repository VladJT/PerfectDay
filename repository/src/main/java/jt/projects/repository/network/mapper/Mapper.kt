package jt.projects.repository.network.mapper

import jt.projects.model.*
import jt.projects.repository.network.vk.model.VkFriendResponse
import jt.projects.repository.network.vk.model.VkInfoResponse
import jt.projects.repository.network.vk.model.VkUserResponse

internal fun VkInfoResponse.toVkInfo(): VkInfo = this.run {
    VkInfo(
        count = count,
        friends = items.map {
            it.toVkFriend()
        }
    )
}

internal fun VkFriendResponse.toVkFriend(): VkFriend = this.run {
    VkFriend(
        id = id,
        bdate = bdate,
        photoUrl = photo200_Orig,
        firstName = firstName,
        lastName = lastName,
        canAccessClosed = canAccessClosed,
        isClosed = isClosed,
        deactivated = deactivated
    )
}

internal fun VkUserResponse.toVkUserInfo(): VkUserInfo = this.run {
    VkUserInfo(
        photoMax = photoMax,
        firstName = firstName,
        lastName = lastName
    )
}