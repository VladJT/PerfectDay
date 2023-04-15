package jt.projects.model


// состояния приложения
sealed class AppState {
    data class Success(val data: List<DataModel>?) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class Loading(val progress: Int?, val status: String? = null) : AppState()
}