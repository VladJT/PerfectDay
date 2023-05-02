package jt.projects.utils.extensions

import jt.projects.model.*

fun DataModel.BirthdayFromVk.toFriend(): DataModel.Friend = this.run {
    DataModel.Friend(
        idVk = vkId,
        idPhone = null,
        type = FriendType.VK,
        name = name,
        birthDate = birthDate,
        age = age,
        photoUrl = photoUrl
    )
}

fun DataModel.BirthdayFromPhone.toFriend(): DataModel.Friend = this.run {
    DataModel.Friend(
        idVk = null,
        idPhone = id,
        type = FriendType.PHONE,
        name = name,
        birthDate = birthDate,
        age = age ?: 0,
        photoUrl = photoUri ?: emptyString()
    )
}