package jt.projects.repository.push

import android.provider.ContactsContract.Data

class PushBirthdayRepoImpl:PushBirthdayRepo {
    override fun getDataPushBirthday(): List<DataPush> {
        return emptyList()
    }

    override fun getDataTest(): List<DataPush> {
        return listOf(
            DataPush(
            "test",
            "test",
            null

        )
        )
    }
}