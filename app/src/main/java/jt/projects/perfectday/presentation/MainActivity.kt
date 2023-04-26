package jt.projects.perfectday.presentation

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.progressindicator.LinearProgressIndicator
import jt.projects.model.DataModel
import jt.projects.perfectday.R
import jt.projects.perfectday.databinding.ActivityMainBinding
import jt.projects.perfectday.presentation.calendar.CalendarFragment
import jt.projects.perfectday.presentation.intro.IntroActivity
import jt.projects.perfectday.presentation.reminder.ReminderFragment
import jt.projects.perfectday.presentation.schedule_event.ScheduleEventFragment
import jt.projects.perfectday.presentation.settings.SettingsFragment
import jt.projects.perfectday.presentation.today.TodayFragment
import jt.projects.utils.IS_FIRST_TIME_START_APP_KEY
import jt.projects.utils.REQUEST_CODE_READ_CONTACTS
import jt.projects.utils.extensions.showSnackbar
import jt.projects.utils.network.OnlineStatusLiveData
import jt.projects.utils.permissionGranted
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import jt.projects.utils.toStdFormatString
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import java.time.LocalDate


class MainActivity : AppCompatActivity() {
    companion object {
        const val ANIMATION_DURATION = 500L
    }

    private lateinit var binding: ActivityMainBinding


    private val settingsPreferences by inject<SimpleSettingsPreferences>()


    override fun onCreate(savedInstanceState: Bundle?) {
        // убираем splash screen для Android 10 и ниже
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            setTheme(R.style.Theme_PerfectDay)
        }

        super.onCreate(savedInstanceState)

        if (isFirstTimeStartApp()) {
            startIntoActivity()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBottomNavView()
        setOnClickSettings()
        initFab()
        checkPermission()
        initButtonBackHome()
        //    subscribeToNetworkStatusChange()
    }

    private fun startIntoActivity() {
        startActivity(Intent(this, IntroActivity::class.java))
    }

    private fun isFirstTimeStartApp(): Boolean {
        val result = settingsPreferences.getSettings(IS_FIRST_TIME_START_APP_KEY)
        return if (!result.equals("false")) {
            settingsPreferences.saveSettings(IS_FIRST_TIME_START_APP_KEY, "false")
            true
        } else {
            false
        }
    }

    private fun initButtonBackHome() {
        binding.layoutToolbar.btnBack.setOnClickListener {
            supportFragmentManager.popBackStack()
        }
    }

    private fun initFab() {
        binding.fBtnAddReminder.setOnClickListener {
            navigateToFragment(
                ScheduleEventFragment.newInstance(LocalDate.now().toStdFormatString()),
                isAddToBackStack = true
            )
        }
    }

    fun showScheduledEvent(id: Int) {
        navigateToFragment(
            ScheduleEventFragment.newInstance(id),
            isAddToBackStack = true
        )
    }

    fun showFab(isShow: Boolean) {
        if (isShow) {
            binding.fBtnAddReminder.visibility = View.VISIBLE
        } else {
            binding.fBtnAddReminder.visibility = View.GONE
        }
    }

    fun showButtonBackHome(isVisible: Boolean) {
        if (isVisible) {
            binding.layoutToolbar.btnBack.visibility = View.VISIBLE
            binding.layoutToolbar.btnBack.animate()
                .alpha(1f)
                .setInterpolator(LinearInterpolator()).duration = ANIMATION_DURATION
        } else {
            binding.layoutToolbar.btnBack.visibility = View.GONE
            binding.layoutToolbar.btnBack.alpha = 0f
        }
    }

    private fun initBottomNavView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            onOptionsItemSelected(item)
            true
        }
    }

    private fun setOnClickSettings() {
        binding.layoutToolbar.btnSettings.setOnClickListener {
            it.animate()
                .rotationBy(180f)
                .setInterpolator(LinearInterpolator()).duration = ANIMATION_DURATION
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
                navigateToFragment(TodayFragment())
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
        // проверяем,что фрагмент еще не запущен
        if (supportFragmentManager.fragments.find { it.javaClass == fragment::class.java } != null) return

        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.replace(binding.fragmentContainer.id, fragment)

        if (isAddToBackStack) {
            beginTransaction.addToBackStack(null)
        }
        beginTransaction
            //.setCustomAnimations(R.animator.std_left, R.animator.std_right)
            .commit()
    }


    fun showProgress(progress: Int, status: String? = null) {
        val hProgressBar = findViewById<LinearProgressIndicator>(R.id.progress_bar_horizontal)
        val animator =
            ObjectAnimator.ofInt(hProgressBar, "progress", hProgressBar.progress, progress)
        animator.interpolator = DecelerateInterpolator(2f)
        animator.start()
        hProgressBar.progress = progress

        status?.let {
            findViewById<TextView>(R.id.tv_progress_bar_label).text = it
        }
    }

    private fun checkPermission() {
        val permResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        if (permResult == PackageManager.PERMISSION_GRANTED) {
            permissionGranted = true
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            AlertDialog.Builder(this)
                .setTitle("Доступ к контактам")
                .setMessage("Запрос на доступ к контактам. В случае отказа, доступ можно будет предоставить только в настройках приложения.")
                .setPositiveButton("Открыть окно предоставления доступа") { _, _ ->
                    permissionRequest(Manifest.permission.READ_CONTACTS)
                }
                .setNegativeButton("Отказать в запросе") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        } else {
            permissionRequest(Manifest.permission.READ_CONTACTS)
        }
    }

    private fun permissionRequest(permission: String) {
        requestPermissions(arrayOf(permission), REQUEST_CODE_READ_CONTACTS)
    }


}