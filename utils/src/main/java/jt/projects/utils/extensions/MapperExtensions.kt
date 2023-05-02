package jt.projects.utils.extensions

import jt.projects.model.*

fun DataModel.BirthdayFromVk.toFriend(): DataModel.Friend = this.run {
    DataModel.Friend(
        id = vkId.toString(),
        type = FriendType.VK,
        name = name,
        birthDate = birthDate,
        age = age,
        photoUrl = photoUrl
    )
}

fun DataModel.BirthdayFromPhone.toFriend(): DataModel.Friend = this.run {
    DataModel.Friend(
        id = id,
        type = FriendType.PHONE,
        name = name,
        birthDate = birthDate,
        age = age ?: 0,
        photoUrl = photoUri ?: emptyString()
    )
}