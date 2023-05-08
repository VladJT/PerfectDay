package jt.projects.perfectday.presentation

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.MenuItem
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import androidx.transition.AutoTransition
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.google.android.material.progressindicator.LinearProgressIndicator
import jt.projects.perfectday.R
import jt.projects.perfectday.core.GlobalViewModel
import jt.projects.perfectday.core.toStdFormatString
import jt.projects.perfectday.core.translator.GoogleTranslator
import jt.projects.perfectday.core.translator.TranslatorCallback
import jt.projects.perfectday.databinding.ActivityMainBinding
import jt.projects.perfectday.presentation.calendar.CalendarFragment
import jt.projects.perfectday.presentation.intro.IntroActivity
import jt.projects.perfectday.presentation.reminder.ReminderFragment
import jt.projects.perfectday.presentation.schedule_event.ScheduleEventFragment
import jt.projects.perfectday.presentation.settings.PushSettingFragment
import jt.projects.perfectday.presentation.settings.SettingsFragment
import jt.projects.perfectday.presentation.today.TodayFragment
import jt.projects.utils.IS_FIRST_TIME_START_APP_KEY
import jt.projects.utils.LOG_TAG
import jt.projects.utils.REQUEST_CODE_READ_CONTACTS
import jt.projects.utils.extensions.showSnackbar
import jt.projects.utils.extensions.showToast
import jt.projects.utils.network.OnlineStatusLiveData
import jt.projects.utils.permissionGranted
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import java.time.LocalDate


class MainActivity : AppCompatActivity() {
    companion object {
        const val ANIMATION_DURATION = 500L
    }

    private lateinit var binding: ActivityMainBinding

    private val settingsPreferences by inject<SimpleSettingsPreferences>()

    private val fragmentLifecycleCallback = object : FragmentLifecycleCallbacks() {
        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            super.onFragmentResumed(fm, f)
            when (f) {
                is SettingsFragment -> {
                    showButtonBackHome(true)
                    binding.fBtnAddReminder.isVisible = false
                    showButtonSettings(false)
                }

                is PushSettingFragment -> {
                    showButtonBackHome(true)
                    binding.fBtnAddReminder.isVisible = false
                    showButtonSettings(false)
                }

                is ScheduleEventFragment -> {
                    showButtonBackHome(true)
                    binding.fBtnAddReminder.isVisible = false
                    showButtonSettings(true)
                }

                else -> {
                    showButtonBackHome(false)
                    binding.fBtnAddReminder.isVisible = true
                    showButtonSettings(true)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // убираем splash screen для Android 10 и ниже
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            setTheme(R.style.Theme_PerfectDay)
        }

        super.onCreate(savedInstanceState)

        // фоновая загрузка GlobalViewModel
        CoroutineScope(Dispatchers.IO).launch {
            val vm = getKoin().get<GlobalViewModel>()
        }

        if (isFirstTimeStartApp()) {
            startIntoActivity()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleCallback, false)

        subscribeToNetworkStatusChange()
        initBottomNavView()
        setOnClickSettings()
        initFab()
        checkPermission()
        initButtonBackHome()
        initGoogleTranslator()
    }

    private fun initGoogleTranslator() {
        try {
            get<GoogleTranslator>().downloadModelIfNeeded(object : TranslatorCallback {
                override fun onSuccess(result: String?) {
                    //    showToast(getString(R.string.translation_loading))
                }

                override fun onFailure(result: String?) {
                    showToast(getString(R.string.translation_error_loading))
                }
            })
        } catch (e: Exception) {
            Log.d(LOG_TAG, e.message.toString())
        }
    }


    private fun startIntoActivity() {
        startActivity(Intent(this, IntroActivity::class.java))
    }

    private fun isFirstTimeStartApp(): Boolean {
        val isFirstStart = settingsPreferences.getBooleanOrTrue(IS_FIRST_TIME_START_APP_KEY)
        return if (isFirstStart) {
            settingsPreferences.saveBoolean(IS_FIRST_TIME_START_APP_KEY, false)
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
            ScheduleEventFragment.newInstance(id), isAddToBackStack = true
        )
    }

    fun showButtonBackHome(isVisible: Boolean) {
        val btnHomeTransition = TransitionSet().apply {
            val transition = Fade()
            transition.duration = ANIMATION_DURATION
            addTransition(transition)
        }
        if (isVisible) {
            TransitionManager.beginDelayedTransition(
                binding.layoutToolbar.toolbarLayout, btnHomeTransition
            )
        }
        binding.layoutToolbar.btnBack.isVisible = isVisible
    }

    fun showButtonSettings(isVisible: Boolean) {
        val btnSettingsTransition = TransitionSet().apply {
            val transition = AutoTransition()
            transition.duration = ANIMATION_DURATION
            addTransition(transition)
        }

        TransitionManager.beginDelayedTransition(
            binding.layoutToolbar.toolbarLayout, btnSettingsTransition
        )

        if (isVisible) {
            binding.layoutToolbar.btnSettings.run {
                animate().alpha(1f)
                isClickable = true
            }
        } else {
            binding.layoutToolbar.btnSettings.run {
                animate().rotationBy(180f).alpha(0.1f)
                    .setInterpolator(LinearInterpolator()).duration = ANIMATION_DURATION
                isClickable = false
            }
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
            navigateToFragment(SettingsFragment(), isAddToBackStack = true)
        }
    }

    private fun subscribeToNetworkStatusChange() {
        getKoin().get<OnlineStatusLiveData>().observe(this@MainActivity) { isOnline ->
            if (!isOnline) {
                showSnackbar(getString(R.string.no_internet))
            }
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

    fun navigateToFragment(fragment: Fragment, isAddToBackStack: Boolean = false) {
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
        val permResult = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        if (permResult == PackageManager.PERMISSION_GRANTED) {
            permissionGranted = true
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            AlertDialog.Builder(
                ContextThemeWrapper(
                    this, R.style.PerfectDay_MaterialCalendarTheme
                )
            ).setTitle(getString(R.string.alert_contacts_title))
                .setMessage(getString(R.string.alert_contacts_message))
                .setPositiveButton(getString(R.string.open_permission_settings)) { _, _ ->
                    permissionRequest(Manifest.permission.READ_CONTACTS)
                }
                .setNegativeButton(getString(R.string.close_permission_settings)) { dialog, _ -> dialog.dismiss() }
                .create().show()
        } else {
            permissionRequest(Manifest.permission.READ_CONTACTS)
        }
    }

    private fun permissionRequest(permission: String) {
        requestPermissions(arrayOf(permission), REQUEST_CODE_READ_CONTACTS)
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentLifecycleCallback)
        super.onDestroy()
    }

}