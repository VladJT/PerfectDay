package jt.projects.repository.push

import android.content.Context
import jt.projects.model.DataModel
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.koin.core.scope.get

class PushBirthdayRepoImpl(

) : PushBirthdayRepo, KoinComponent {

    val context:Context = get()

    private fun handleError(throwable: Throwable) {

    }

    override fun getDataPushBirthday(): List<DataPush> {
        return emptyList()
    }

    override fun getDataTest(): List<DataPush> {
        return listOf(
            DataPush(
                "channel_birthday",
                "test",
                "test"
            ),
            DataPush(
                "channel_event",
                "test",
                "test"

            )
        )
    }
}