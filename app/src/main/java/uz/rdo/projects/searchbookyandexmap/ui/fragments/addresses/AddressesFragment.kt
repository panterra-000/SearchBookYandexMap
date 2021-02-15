package uz.rdo.projects.searchbookyandexmap.ui.fragments.addresses

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import uz.rdo.projects.searchbookyandexmap.data.room.db.MyDataBase
import uz.rdo.projects.searchbookyandexmap.data.room.entity.PlaceModel
import uz.rdo.projects.searchbookyandexmap.databinding.FragmentAddressesBinding
import uz.rdo.projects.searchbookyandexmap.ui.baseFactories.AddressesViewModelFactory

class AddressesFragment : Fragment() {

    private var _binding: FragmentAddressesBinding? = null
    private val binding: FragmentAddressesBinding
        get() = _binding ?: throw NullPointerException("dddd null pointer exception")

    lateinit var viewModel: AddressesViewModel

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
        setupViewModel()
        loadObservers()
        loadViews()
        viewModel.getAllPlacesList()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun loadObservers() {
        viewModel.resultPlacesList.observe(this, getPlacesListObserver)
        viewModel.resultDeleteAllPlaces.observe(this, delPlaceListObserver)
    }


    private val getPlacesListObserver = Observer<List<PlaceModel>> { placesList ->
        var result: String = ""
        for (placeModel in placesList) {
            result += "${placeModel.title} - ${placeModel.id} \n"
        }
        binding.txtResult.text = result
    }

    private val delPlaceListObserver = Observer<Int> {
        if (it >= 0) {
            binding.txtResult.text = ""
        }
    }


    private fun loadViews() {
        binding.btnDelAll.setOnClickListener {
            viewModel.deleteAllPlaceList()
        }
    }

    private fun setupViewModel() {
        val dao = MyDataBase.getDatabase().placeModelDao()
        val repositoryImpl = AddressesRepositoryImpl(dao)
        viewModel = ViewModelProviders.of(
            this,
            AddressesViewModelFactory(repositoryImpl)
        ).get(AddressesViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}