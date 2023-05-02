package jt.projects.utils.extensions

import jt.projects.model.*

fun DataModel.BirthdayFromVk.toFriend(): Friend = this.run {
    Friend(
        idVk = vkId,
        idPhone = null,
        type = FriendType.VK,
        name = name,
        birthDate = birthDate,
        age = age,
        photoUrl = photoUrl
    )
}

fun DataModel.BirthdayFromPhone.toFriend(): Friend = this.run {
    Friend(
        idVk = null,
        idPhone = id,
        type = FriendType.PHONE,
        name = name,
        birthDate = birthDate,
        age = age ?: 0,
        photoUrl = photoUri ?: emptyString()
    )
}