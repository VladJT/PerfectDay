package jt.projects.perfectday.interactors

import jt.projects.model.DataModel
import jt.projects.model.VkFriend
import jt.projects.repository.network.vk.VkNetworkRepository
import jt.projects.utils.extensions.emptyString
import jt.projects.perfectday.core.isPeriodBirthdayDate
import java.time.*
import java.time.format.*

class GetFriendsFromVkUseCase(
    private val vkNetworkRepository: VkNetworkRepository
) {
    private val sortComparatorByMonthAndDay = Comparator<DataModel.BirthdayFromVk> { o1, o2 ->
        val date1 = o1.birthDate
        val date2 = o2.birthDate
        val month1 = date1.monthValue
        val month2 = date2.monthValue

        if (month1 == month2) return@Comparator date1.dayOfMonth.compareTo(date2.dayOfMonth)
        return@Comparator month1.compareTo(month2)
    }

    suspend fun getFriendsByDate(
        userToken: String?,
        date: LocalDate
    ): List<DataModel.BirthdayFromVk> {
        val formatter = DateTimeFormatter.ofPattern("dd.MM")
        return getAllFriends(userToken)
            .filter { it.birthDate.format(formatter) == date.format(formatter) }
    }

    suspend fun getFriendsByPeriodDate(
        userToken: String?,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<DataModel.BirthdayFromVk> =
        getAllFriends(userToken)
            .filter { isPeriodBirthdayDate(startDate, endDate, it.birthDate) }
            .sortedWith(sortComparatorByMonthAndDay)

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
            vkId = id,
            name = "$firstName $lastName",
            birthDate = birthDate,
            age = getAge(birthDate),
            photoUrl = photoUrl ?: emptyString()
        )
    }

    private fun getAge(birthDate: LocalDate): Int =
        Period.between(birthDate, LocalDate.now()).years.plus(1)
}