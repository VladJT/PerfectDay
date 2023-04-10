package jt.projects.perfectday.presentation

import android.os.Bundle
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import jt.projects.perfectday.R
import jt.projects.perfectday.databinding.ActivityMainBinding
import jt.projects.perfectday.presentation.calendar.CalendarFragment
import jt.projects.perfectday.presentation.reminder.ReminderFragment
import jt.projects.perfectday.presentation.settings.SettingsFragment
import jt.projects.perfectday.presentation.today.TodayFragment
import jt.projects.utils.network.OnlineStatusLiveData
import jt.projects.utils.showSnackbar
import org.koin.android.ext.android.getKoin

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBottomNavView()
        setOnClickSettings()
        //    subscribeToNetworkStatusChange()
    }

    private fun initBottomNavView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            onOptionsItemSelected(item)
            true
        }
    }

    private fun setOnClickSettings() {
        binding.layoutToolbar.btnSettings.setOnClickListener {
            navigateToFragment(SettingsFragment(), isAddToBackStack = true)
        }
    }
    private fun subscribeToNetworkStatusChange() {
        getKoin()
            .get<OnlineStatusLiveData>()
            .observe(this@MainActivity) { isOnline ->
                this@MainActivity.showSnackbar("Internet: $isOnline")
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_action_today -> {
                navigateToFragment(TodayFragment.newInstance())
            }

            R.id.menu_action_reminder -> {
                navigateToFragment(ReminderFragment.newInstance())
            }

            R.id.menu_action_calendar -> {
                navigateToFragment(CalendarFragment.newInstance())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToFragment(fragment: Fragment, isAddToBackStack: Boolean = false) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.replace(binding.fragmentContainer.id, fragment)

        if (isAddToBackStack) {
            beginTransaction.addToBackStack(null)
        }
        beginTransaction
            //.setCustomAnimations(R.animator.std_left, R.animator.std_right)
            .commit()
    }

    fun showProgress(progress: Int) {
        findViewById<ProgressBar>(R.id.progress_bar_horizontal).progress = progress
    }
}