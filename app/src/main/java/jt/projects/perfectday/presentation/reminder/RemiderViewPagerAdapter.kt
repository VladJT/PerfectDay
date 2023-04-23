package jt.projects.perfectday.presentation.reminder

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import jt.projects.perfectday.presentation.reminder.ReminderFragment.Companion.LEFT_CHILD
import jt.projects.perfectday.presentation.reminder.ReminderFragment.Companion.RIGHT_CHILD

// конструктор мы теперь передаем корневую Активити (или фрагмент), хотя
//можно передавать FragmentManager вместе с Lifecycle, но первый способ предпочтительнее
class RemiderViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    private val fragments =
        arrayOf(LeftChildFragment(), RightChildFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> fragments[LEFT_CHILD]
            1 -> fragments[RIGHT_CHILD]
            else -> fragments[LEFT_CHILD]
        }
    }
}