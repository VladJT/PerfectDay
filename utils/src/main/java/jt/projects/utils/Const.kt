package jt.projects.utils



/**
 * EXCEPTIONS
 */
val ViewModelNotInitException = IllegalStateException("The ViewModel should be initialised first")
val InteractorException = IllegalStateException("Something wrong in interactor")


/**
 * LOGS
 */
const val LOG_TAG = "TAG"


/**
 * DI
 */
const val NAME_REMOTE = "Remote"
const val NAME_LOCAL = "Local"
const val NETWORK_SERVICE = "NETWORK_SERVICE"


/**
 * BUILD CONFIG
 */
const val FAKE = "FAKE"
const val REAL = "REAL"
