package jt.projects.repository.network.mapper

import jt.projects.model.*
import jt.projects.repository.network.model.*

internal fun VkResponse.toVkInfo(): VkInfo = this.run {
    VkInfo(
        count = response.count,
        friends = response.items.map {
            it.toVkFriend()
        }
    )
}

internal fun VkFriendResponse.toVkFriend(): VkFriend = this.run {
    VkFriend(
        id = id,
        bdate = bdate,
        photo200_Orig = photo200_Orig,
        firstName = firstName,
        lastName = lastName,
        canAccessClosed = canAccessClosed,
        isClosed = isClosed,
        deactivated = deactivated
    )
}