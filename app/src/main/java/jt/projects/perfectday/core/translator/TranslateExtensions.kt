package jt.projects.perfectday.core.translator

import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

fun TextView.translateText() {
    val translator = KoinJavaComponent.getKoin().get<GoogleTranslator>()

    val text = this.text.toString()

    CoroutineScope(Dispatchers.Main).launch {
        translator.isTranslationModelOk.onEach { isLanguageDownloaded ->
            if (isLanguageDownloaded) {
                translator.translate(text).onEach {
                    this@translateText.text = it
                }.collect()
            }
        }.collect()
    }
}


suspend fun String.translateText(): String {
    val translator = KoinJavaComponent.getKoin().get<GoogleTranslator>()
    var translatedText = this

    CoroutineScope(Dispatchers.Default).async {
        val isLanguageDownloaded = translator.isTranslationModelOk.value

        // если языковой пакет загружен - переводим
        if (isLanguageDownloaded) {
            CoroutineScope(Dispatchers.Default).async {
                translator.translate(this@translateText).collect {
                    translatedText = it
                }
            }.await()
        }
        // если языковой пакет НЕ загружен - оставляем без перевода
        else {
            return@async
        }
    }.await()

    return translatedText
}