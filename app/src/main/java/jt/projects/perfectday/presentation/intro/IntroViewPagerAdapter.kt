package jt.projects.perfectday.presentation.intro

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import jt.projects.perfectday.presentation.intro.IntroActivity.Companion.FIRST_INTRO_FRAGMENT
import jt.projects.perfectday.presentation.intro.IntroActivity.Companion.SECOND_INTRO_FRAGMENT
import jt.projects.perfectday.presentation.intro.IntroActivity.Companion.THIRD_FRAGMENT

// конструктор мы теперь передаем корневую Активити (или фрагмент), хотя
//можно передавать FragmentManager вместе с Lifecycle, но первый способ предпочтительнее
class IntroViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragments =
        arrayOf(FragmentIntroFirst(), FragmentIntroSecond(), FragmentIntroThird())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> fragments[FIRST_INTRO_FRAGMENT]
            1 -> fragments[SECOND_INTRO_FRAGMENT]
            2 -> fragments[THIRD_FRAGMENT]
            else -> fragments[FIRST_INTRO_FRAGMENT]
        }
    }

}