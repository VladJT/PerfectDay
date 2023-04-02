package jt.projects.perfectday.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import jt.projects.perfectday.R
import jt.projects.perfectday.databinding.ActivityMainBinding
import jt.projects.perfectday.presentation.calendar.CalendarFragment
import jt.projects.perfectday.presentation.reminder.ReminderFragment
import jt.projects.perfectday.presentation.today.TodayFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            replaceFragment(TodayFragment.newInstance())
        }

        initToolBar()
        initBottomNavView()
    }

    private fun initToolBar() {
        binding.toolbar.title = getString(R.string.app_name)
        setSupportActionBar(binding.toolbar)
    }

    private fun initBottomNavView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            onOptionsItemSelected(item)
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.menu_action_today -> {
                replaceFragment(TodayFragment.newInstance())
            }

            R.id.menu_action_reminder -> {
                replaceFragment(ReminderFragment.newInstance())
            }

            R.id.menu_action_calendar -> {
                replaceFragment(CalendarFragment.newInstance())
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            //.setCustomAnimations(R.animator.std_left, R.animator.std_right)
            .replace(binding.fragmentContainer.id, fragment)
            .commitNow()
    }
}