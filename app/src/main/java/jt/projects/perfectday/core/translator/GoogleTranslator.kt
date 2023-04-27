package jt.projects.perfectday.core.translator


import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import java.util.concurrent.CopyOnWriteArrayList

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

    fun translate(text: String): Flow<String> {
        return createCallbackFlow(FlowCallback(text, translator))
    }

    private fun createCallbackFlow(flowCallback: FlowCallback) = callbackFlow<String> {
        val listener = object : FlowCallback.StringCallback {
            override fun onSuccess(result: String) {
                trySend(result)
            }

            override fun onFailure(result: String) {
                trySend(result)
            }
        }

        flowCallback.addListener(listener)
        flowCallback.invoke()

        awaitClose {
            flowCallback.removeListener(listener)
        }
    }

}

class FlowCallback(private val text: String, private val translator: Translator) {
    private val listeners = CopyOnWriteArrayList<StringCallback>()
    fun addListener(l: StringCallback) = listeners.add(l)
    fun removeListener(l: StringCallback) = listeners.remove(l)

    fun invoke() {
        listeners.forEach {
            translator.translate(text)
                .addOnSuccessListener { translatedText ->
                    it.onSuccess(translatedText)
                }
                .addOnFailureListener { exception ->
                    it.onFailure(exception.message.toString())
                }
        }
    }

    interface StringCallback {
        fun onSuccess(result: String)
        fun onFailure(result: String)
    }
}