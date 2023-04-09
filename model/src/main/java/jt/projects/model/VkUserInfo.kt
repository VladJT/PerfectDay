package jt.projects.model

data class VkUserInfo(
    val photoMax: String,
    val firstName: String,
    val lastName: String
) {
    companion object {
        val EMPTY = VkUserInfo("", "User not loaded", "")
    }
}