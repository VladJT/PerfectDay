package jt.projects.perfectday.core

import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import jt.projects.model.DataModel
import jt.projects.perfectday.core.extensions.showScheduledEvent
import jt.projects.utils.extensions.showToast

abstract class BaseFragment : Fragment() {
    val baseAdapter: BaseAdapter by lazy { BaseAdapter(::onItemClick, ::onItemDelete) }

    abstract val viewModel: BaseViewModel

    private fun onItemClick(data: DataModel) {
        when (data) {
            is DataModel.ScheduledEvent -> {
                showScheduledEvent(data)
            }
            is DataModel.BirthdayFromPhone -> {
                congratulateContact(data)
            }
            else -> {
                requireActivity().showToast(data.toString())
            }
        }
    }

    // поздравляем контакт
    private fun congratulateContact(data: DataModel.BirthdayFromPhone) {
        val contactId = data.id //  ID контакта
        val contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId)
        val intent = Intent(Intent.ACTION_VIEW, contactUri)
        startActivity(intent)
    }

    // удаление запланированного события
    private fun onItemDelete(data: DataModel, position: Int) {
        if (data is DataModel.ScheduledEvent) {
            viewModel.deleteScheduledEvent(data.id)
            baseAdapter.notifyItemRemoved(position)
        }
    }
}