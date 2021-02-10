package uz.rdo.projects.searchbookyandexmap.ui.fragments.addresses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.rdo.projects.searchbookyandexmap.R
import uz.rdo.projects.searchbookyandexmap.databinding.FragmentAddressesBinding


class AddressesFragment : Fragment() {

    private var _binding: FragmentAddressesBinding? = null
    private val binding: FragmentAddressesBinding
        get() = _binding ?: throw NullPointerException("dddd null pointer exception")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddressesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}