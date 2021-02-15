package uz.rdo.projects.searchbookyandexmap.ui.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import uz.rdo.projects.searchbookyandexmap.R
import uz.rdo.projects.searchbookyandexmap.data.room.entity.PlaceModel
import uz.rdo.projects.searchbookyandexmap.databinding.DialogAddAddressBinding
import uz.rdo.projects.searchbookyandexmap.databinding.DialogDelAddressBinding
import uz.rdo.projects.searchbookyandexmap.utils.SingleBlock
import uz.rdo.projects.searchbookyandexmap.utils.showToast

class AddressDeleteDialog(private val activity: Activity, private val placeModel: PlaceModel) :
    AlertDialog(activity) {

    private var _binding: DialogDelAddressBinding? = null
    private val binding: DialogDelAddressBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")


    private var editable: Boolean = false

    private var listenClick: SingleBlock<PlaceModel>? = null


    init {
        _binding = DialogDelAddressBinding.inflate(layoutInflater)
        setView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadViews()
    }

    private fun loadViews() {
        binding.apply {
            etNameAddress.setText(placeModel.title)
            etNameAddress.isEnabled = editable

            btnCancel.setOnClickListener {
                dismiss()
            }

            btnDelete.setOnClickListener {
                placeModel.title = binding.etNameAddress.text.toString()
                listenClick?.invoke(placeModel)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        _binding = null
    }

    fun setOnclickDeleteCallback(f: SingleBlock<PlaceModel>) {
        listenClick = f
    }




}