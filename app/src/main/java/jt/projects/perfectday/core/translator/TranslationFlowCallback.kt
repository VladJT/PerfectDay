package jt.projects.perfectday.core.translator

import com.google.mlkit.nl.translate.Translator
import java.util.concurrent.CopyOnWriteArrayList

class TranslationFlowCallback(private val text: String, private val translator: Translator) {
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