package jt.projects.perfectday.core.translator


import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface TranslatorCallback {
    fun onSuccess(result: String? = null)
    fun onFailure(result: String? = null)
}

class GoogleTranslator {
    private val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.RUSSIAN)
        .build()

    private val translator = Translation.getClient(options)

    private val _isTranslationModelOk = MutableStateFlow(false)
    val isTranslationModelOk get() = _isTranslationModelOk.asStateFlow()

    fun downloadModelIfNeeded(callback: TranslatorCallback) {
        var conditions = DownloadConditions.Builder()
            //    .requireWifi()
            .build()
        translator.downloadModelIfNeeded(conditions)

        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
                callback.onSuccess()
                _isTranslationModelOk.tryEmit(true)
            }
            .addOnFailureListener {
                callback.onFailure()
                _isTranslationModelOk.tryEmit(false)
            }
    }

    fun translate(text: String, callback: TranslatorCallback) {
        translator.translate(text)
            .addOnSuccessListener { translatedText ->
                callback.onSuccess(translatedText)
            }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message.toString())
            }
    }


}