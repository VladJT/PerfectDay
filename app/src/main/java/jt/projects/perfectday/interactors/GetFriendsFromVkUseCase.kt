package jt.projects.perfectday.interactors

import jt.projects.model.*
import jt.projects.repository.network.vk.VkNetworkRepository
import jt.projects.utils.extensions.emptyString
import java.time.*
import java.time.format.*

class GetFriendsFromVkUseCase(
    private val vkNetworkRepository: VkNetworkRepository
) {

    suspend fun getAllFriends(userToken: String?): List<DataModel.BirthdayFromVk> {
        if (userToken == null || userToken.isEmpty()) return emptyList()
        val vkInfo = vkNetworkRepository.getUserFriends(userToken)
        return createBirthdayFromVkList(vkInfo.friends)
    }

    private fun createBirthdayFromVkList(friends: List<VkFriend>): List<DataModel.BirthdayFromVk> {
        val result = mutableListOf<DataModel.BirthdayFromVk>()

        friends.forEach {
            val birthDate = tryParseDate(it.bdate)
            if (birthDate == LocalDate.MIN) return@forEach

            result.add(toBirthdayFromVk(it, birthDate))
        }
        return result
    }

    private fun tryParseDate(date: String?): LocalDate {
        if (date == null) return LocalDate.MIN

        return try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("d.M.yyyy"))
        } catch (e: DateTimeParseException) {
            LocalDate.MIN
        }
    }

    private fun toBirthdayFromVk(
        friend: VkFriend,
        birthDate: LocalDate
    ): DataModel.BirthdayFromVk = friend.run {
        DataModel.BirthdayFromVk(
            name = "$firstName $lastName",
            birthDate = birthDate,
            age = getAge(birthDate),
            photoUrl = photoUrl ?: emptyString()
        )
    }

    private fun getAge(birthDate: LocalDate): Int =
        Period.between(birthDate, LocalDate.now()).years
}