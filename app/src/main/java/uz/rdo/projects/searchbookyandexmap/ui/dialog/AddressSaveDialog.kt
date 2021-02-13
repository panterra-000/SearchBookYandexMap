package uz.rdo.projects.searchbookyandexmap.ui.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import uz.rdo.projects.searchbookyandexmap.data.room.entity.PlaceModel
import uz.rdo.projects.searchbookyandexmap.databinding.DialogAddAddressBinding
import uz.rdo.projects.searchbookyandexmap.utils.showToast

class AddressSaveDialog(private val activity: Activity, private val placeModel: PlaceModel) :
    AlertDialog(activity) {

    private var _binding: DialogAddAddressBinding? = null
    private val binding: DialogAddAddressBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private var editable: Boolean = false

    init {
        _binding = DialogAddAddressBinding.inflate(layoutInflater)
        setView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadViews()
    }

    private fun loadViews() {
        binding.apply {
            etNameAddress.setText(placeModel.title)
            imgEdit.setOnClickListener {
                    editable = !editable
                    etNameAddress.isEnabled = editable
            }

            btnCancel.setOnClickListener {
                dismiss()
            }

            btnSave.setOnClickListener {
                showToast("Saved ...", activity)
            }


        }
    }
}