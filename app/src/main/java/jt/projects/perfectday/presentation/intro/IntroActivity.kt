package jt.projects.perfectday.presentation.intro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import jt.projects.perfectday.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    companion object {
        const val FIRST_INTRO_FRAGMENT = 0
        const val SECOND_INTRO_FRAGMENT = 1
        const val THIRD_FRAGMENT = 2
    }

    private lateinit var binding: ActivityIntroBinding
    private var counterPageScroll = 0 // смахивание последнего фрагмента закроет активити

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)

        setContentView(binding.root)
        //В activity нам достаточно найти ViewPager в макете, создать адаптер и
        //передать в него SupportFragmentManager.
        binding.viewPager.adapter = IntroViewPagerAdapter(this)
        binding.indicator.setViewPager(binding.viewPager)
        setTabs()
        initButtons()
        initViewPagerListeners()
    }


    private fun initViewPagerListeners() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (position == THIRD_FRAGMENT && positionOffset == 0f) {
                    if (counterPageScroll != 0) {
                        finish()
                    }
                    counterPageScroll++
                } else {
                    counterPageScroll = 0
                }
            }
        })
    }

    private fun initButtons() {
        binding.tvSkipIntro.setOnClickListener { finish() }
    }


    private fun setTabs() {
        //Зато код в Активити заметно упростился. Теперь, чтобы использовать TabView во ViewPager2, вам
        //нужно обратиться к помощи посредника — TabLayoutMediator. Этот класс принимает TabLayout и
        //ViewPager2, а возвращает выбранную пользователем вкладку и ее позицию. Далее мы вкладке (tab)
        //устанавливаем название и иконку. Подсвечивается выбранная вкладка теперь автоматически
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//            tab.text = when (position) {
//                FIRST_INTRO_FRAGMENT -> "1"
//                SECOND_INTRO_FRAGMENT -> "2"
//                THIRD_FRAGMENT -> "3"
//                else -> "-1"
//            }
//            tab.icon = when (position) {
//                FIRST_INTRO_FRAGMENT -> ContextCompat.getDrawable(
//                    this@IntroActivity,
//                    R.drawable.ic_home
//                )
//
//                SECOND_INTRO_FRAGMENT -> ContextCompat.getDrawable(
//                    this@IntroActivity,
//                    R.drawable.ic_reminder
//                )
//
//                THIRD_FRAGMENT -> ContextCompat.getDrawable(
//                    this@IntroActivity,
//                    R.drawable.ic_calendar
//                )
//
//                else -> ContextCompat.getDrawable(this@IntroActivity, R.drawable.ic_home)
            //           }
        }.attach()
    }

}