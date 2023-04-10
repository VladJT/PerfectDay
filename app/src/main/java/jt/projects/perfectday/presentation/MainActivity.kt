package jt.projects.perfectday.presentation

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import jt.projects.perfectday.R
import jt.projects.perfectday.databinding.ActivityMainBinding
import jt.projects.perfectday.presentation.calendar.CalendarFragment
import jt.projects.perfectday.presentation.dialogs.ScheduleEventDialogFragment
import jt.projects.perfectday.presentation.reminder.ReminderFragment
import jt.projects.perfectday.presentation.settings.SettingsFragment
import jt.projects.perfectday.presentation.today.TodayFragment
import jt.projects.utils.REQUEST_CODE_READ_CONTACTS
import jt.projects.utils.network.OnlineStatusLiveData
import jt.projects.utils.permissionGranted
import jt.projects.utils.showSnackbar
import org.koin.android.ext.android.getKoin


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
        initFab()
        checkPermission()
        //    subscribeToNetworkStatusChange()
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

    private fun initFab() {
        binding.fabTop.setOnClickListener {
            ScheduleEventDialogFragment
                .newInstance(data = null)
                .show(supportFragmentManager, ScheduleEventDialogFragment.TAG)
        }
    }
    private fun subscribeToNetworkStatusChange() {
        getKoin()
            .get<OnlineStatusLiveData>()
            .observe(this@MainActivity) { isOnline ->
                this@MainActivity.showSnackbar("Internet: $isOnline")
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

            R.id.menu_action_settings -> {
                replaceFragment(SettingsFragment())
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

    fun showProgress(progress: Int) {
        findViewById<ProgressBar>(R.id.progress_bar_horizontal).progress = progress
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
            AlertDialog.Builder(this)
                .setTitle("Доступ к контактам")
                .setMessage("Доступ к контактам отсутствует. Доступ можно будет предоставить только в настройках приложения.")
                .setPositiveButton("Закрыть сообщение") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun permissionRequest(permission: String) {
        requestPermissions(arrayOf(permission), REQUEST_CODE_READ_CONTACTS)
    }


}