package jt.projects.perfectday.core

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jt.projects.model.DataModel
import jt.projects.perfectday.core.extensions.createMutableSingleEventFlow
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.perfectday.presentation.calendar.dateFragment.Event
import jt.projects.utils.LOG_TAG
import jt.projects.utils.NO_DATA
import jt.projects.utils.PHONE_GROUP_LABEL
import jt.projects.utils.SCHEDULED_EVENT_GROUP_LABEL
import jt.projects.utils.VK_GROUP_LABEL
import jt.projects.utils.isPeriodBirthdayDate
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import jt.projects.utils.shared_preferences.VK_AUTH_TOKEN
import jt.projects.utils.sortComparatorByMonthAndDay
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate

//Создадим базовую ViewModel, куда вынесем общий для всех функционал

open class GlobalViewModel(
    protected val settingsPreferences: SimpleSettingsPreferences,
    private val birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    private val getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    private val scheduledEventInteractor: ScheduledEventInteractorImpl,
    private val phoneBookProvider: PhoneBookProvider
) : ViewModel() {
    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage

    private val _resultRecycler = MutableStateFlow<List<DataModel>>(listOf())
    val resultRecycler get() = _resultRecycler.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading get() = _isLoading.asStateFlow()

    private val _noteIdFlow = createMutableSingleEventFlow<Int>()
    val noteIdFlow get() = _noteIdFlow.asSharedFlow()

    private var job: Job? = null

    init {
        loadAllContent()
    }

    fun getResultRecyclerByPeriod(startDate: LocalDate, endDate: LocalDate) =
        resultRecycler.map {
            it.filter { data ->
                when {
                    (data is DataModel.BirthdayFromVk) ->
                        isPeriodBirthdayDate(startDate, endDate, data.birthDate)

                    (data is DataModel.BirthdayFromPhone) ->
                        isPeriodBirthdayDate(startDate, endDate, data.birthDate)

                    (data is DataModel.ScheduledEvent) ->
                        data.date in startDate..endDate

                    else -> {
                        true
                    }
                }
            }
        }

    private fun loadAllContent() {
        job?.cancel()
        val vkToken: String? = settingsPreferences.getStringOrEmptyString(VK_AUTH_TOKEN)

        _isLoading.tryEmit(true)

        job = viewModelScope.launch {

            //Ждём загрузку всех данных, чтобы пришли(и приводим к нужному типу)
            val friendsPhone = async { loadFriendsFromPhone() }.await()
                .filterIsInstance<DataModel.BirthdayFromPhone>()

            val friendsVk = async { loadFriendsFromVK(vkToken) }.await()
                .filterIsInstance<DataModel.BirthdayFromVk>()


            /*Подписываемся на изменения базы данных(а именно заметок на текущий день)
            * и каждый раз когда база изменяется, создаем items для RecyclerView
            * с нужными данными, после чего emit во фрагмент*/
            loadScheduledEvents()
                .map {
                    val items = mutableListOf<DataModel>()
                    items.apply {
                        addAll(it
                            .sortedBy { it.date }
                            .withHeader(SCHEDULED_EVENT_GROUP_LABEL))

                        addAll(
                            friendsPhone
                                .sortedWith(sortComparatorByMonthAndDay)
                                .withHeader(PHONE_GROUP_LABEL)
                        )

                        addAll(
                            friendsVk
                                .sortedWith(sortComparatorByMonthAndDay)
                                .withHeader(VK_GROUP_LABEL)
                        )

                        addAll(addInfoIfListIsEmpty())
                    }
                }.onEach {
                    _resultRecycler.tryEmit(it)
                    _isLoading.tryEmit(false)
                }.collect()
        }
    }

    private suspend fun loadFriendsFromPhone() = loadContent {
        birthdayFromPhoneInteractor.getContacts()

    }

    private suspend fun loadFriendsFromVK(vkToken: String?) = loadContent {
        getFriendsFromVkUseCase.getAllFriends(vkToken)
    }

    private fun loadScheduledEvents() =
        scheduledEventInteractor.getAllNotes()


    private fun List<DataModel>.withHeader(headerName: String): List<DataModel> {
        if (this.isEmpty()) {
            return this
        }
        val data = mutableListOf<DataModel>(DataModel.SimpleNotice(headerName, ""))
        data.addAll(this)
        return data
    }

    private fun List<DataModel>.addInfoIfListIsEmpty(): List<DataModel> {
        if (this.isEmpty()) {
            return mutableListOf<DataModel>(DataModel.SimpleNotice(NO_DATA, ""))
        }
        return listOf()
    }

    private suspend fun loadContent(listener: suspend () -> List<DataModel>): List<DataModel> =
        try {
            listener.invoke()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(LOG_TAG, "$e")
            listOf()
        }

    fun onDeleteNoteClicked(id: Int) {
        viewModelScope.launch {
            scheduledEventInteractor.deleteScheduledEventById(id)
        }
        statusMessage.value = Event("dismiss")
    }

    fun onEditNoteClicked(idNote: Int) {
        _noteIdFlow.tryEmit(idNote)
        statusMessage.value = Event("dismiss")
    }

    fun onItemClicked(data: DataModel) {
        if (data is DataModel.BirthdayFromPhone) {
            phoneBookProvider.openContact(data)
        }
    }

    fun onSwipeToRefreshMove(): Unit = loadAllContent()
}