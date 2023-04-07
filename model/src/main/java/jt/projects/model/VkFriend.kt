package jt.projects.model

data class VkFriend(
    val id: Long,
    val bdate: String? = null,
    val photo200_Orig: String? = null,
    val firstName: String,
    val lastName: String,
    val canAccessClosed: Boolean,
    val isClosed: Boolean,
    val deactivated: String? = null
)