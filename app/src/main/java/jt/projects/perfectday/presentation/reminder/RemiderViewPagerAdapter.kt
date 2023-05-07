package jt.projects.perfectday.presentation.reminder

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import jt.projects.perfectday.presentation.reminder.ReminderFragment.Companion.PERIOD
import jt.projects.perfectday.presentation.reminder.ReminderFragment.Companion.TOMORROW

// конструктор мы теперь передаем корневую Активити (или фрагмент), хотя
//можно передавать FragmentManager вместе с Lifecycle, но первый способ предпочтительнее
class RemiderViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragments =
        arrayOf(ChildFragment.newInstance(TOMORROW), ChildFragment.newInstance(PERIOD))

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> fragments[TOMORROW]
            1 -> fragments[PERIOD]
            else -> fragments[TOMORROW]
        }
    }
}