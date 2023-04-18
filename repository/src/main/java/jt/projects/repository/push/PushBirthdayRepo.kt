package jt.projects.repository.push

interface PushBirthdayRepo {
    fun getDataPushBirthday():List<DataPush>
    fun getDataTest():List<DataPush>
}