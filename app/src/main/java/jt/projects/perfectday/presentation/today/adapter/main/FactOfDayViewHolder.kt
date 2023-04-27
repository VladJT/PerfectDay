package jt.projects.perfectday.presentation.today.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.perfectday.core.translator.GoogleTranslator
import jt.projects.perfectday.databinding.ItemFactOfDayBinding
import jt.projects.utils.extensions.hideViewInRecycler
import jt.projects.utils.extensions.showViewInRecycler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin

class FactOfDayViewHolder private constructor(
    private val binding: ItemFactOfDayBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup) : this(
        ItemFactOfDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(item: TodayItem.FactOfDay) {
        if (item.list.isEmpty()) {
            itemView.hideViewInRecycler()
            return
        }
        itemView.showViewInRecycler()

        binding.tvFactDescription.text = item.list.first().description

        val translator = getKoin().get<GoogleTranslator>()


        CoroutineScope(Dispatchers.Default).launch {
            translator.isTranslationModelOk.onEach { modelOk ->
                if (modelOk) {
                    translator.translate(item.list.first().description)
                        .onEach {
                            binding.tvFactDescription.text = it
                        }.collect()
                }
            }.collect()
        }
    }
}