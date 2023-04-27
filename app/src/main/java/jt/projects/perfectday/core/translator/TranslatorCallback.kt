package jt.projects.perfectday.core.translator

interface TranslatorCallback {
    fun onSuccess(result: String? = null)
    fun onFailure(result: String? = null)
}