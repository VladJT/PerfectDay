package jt.projects.perfectday.interactors

import jt.projects.model.DataModel
import jt.projects.model.VkFriend
import jt.projects.perfectday.core.getAge
import jt.projects.perfectday.core.isPeriodBirthdayDate
import jt.projects.perfectday.core.tryParseDate
import jt.projects.repository.network.vk.VkNetworkRepository
import jt.projects.utils.DATE_FORMAT_DAY_MONTH_ONLY
import jt.projects.utils.extensions.emptyString
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
        val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DAY_MONTH_ONLY)
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
        if (userToken.isNullOrEmpty()) return emptyList()
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
}