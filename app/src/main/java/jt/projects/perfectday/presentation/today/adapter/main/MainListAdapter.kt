package jt.projects.perfectday.presentation.today.adapter.main

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jt.projects.perfectday.presentation.today.adapter.TodayItem

class MainListAdapter : ListAdapter<TodayItem, RecyclerView.ViewHolder>(TodayDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
   }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}