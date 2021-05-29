package uz.rdo.projects.searchbookyandexmap.ui.fragments.addresses

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import uz.rdo.projects.searchbookyandexmap.data.room.db.MyDataBase
import uz.rdo.projects.searchbookyandexmap.data.room.entity.PlaceModel
import uz.rdo.projects.searchbookyandexmap.databinding.FragmentAddressesBinding
import uz.rdo.projects.searchbookyandexmap.ui.adapters.recycler.MyAddressesAdapter
import uz.rdo.projects.searchbookyandexmap.ui.baseFactories.AddressesViewModelFactory
import uz.rdo.projects.searchbookyandexmap.ui.dialog.AddressDeleteDialog
import uz.rdo.projects.searchbookyandexmap.utils.showToast

class AddressesFragment : Fragment() {

    private var _binding: FragmentAddressesBinding? = null
    private val binding: FragmentAddressesBinding
        get() = _binding ?: throw NullPointerException("dddd null pointer exception")

    lateinit var viewModel: AddressesViewModel
    lateinit var adapter: MyAddressesAdapter
    lateinit var mPlacesList: ArrayList<PlaceModel>
    lateinit var dialog: AddressDeleteDialog

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
        viewModel.resultDeletePlace.observe(this, delPlaceObserver)
    }

    private val getPlacesListObserver = Observer<List<PlaceModel>> { placesList ->
        mPlacesList.clear()
        mPlacesList.addAll(placesList)
        adapter.submitList(mPlacesList)
    }

    private val delPlaceListObserver = Observer<Int> {
        if (it >= 0) {
            adapter.submitList(arrayListOf())
        }
    }

    private val delPlaceObserver = Observer<Boolean> {

    }

    private fun loadViews() {
        binding.btnDelAll.setOnClickListener {
            viewModel.deleteAllPlaceList()
        }
        binding.rvMyPlaces.layoutManager = LinearLayoutManager(requireContext())
        adapter = MyAddressesAdapter(arrayListOf())
        binding.rvMyPlaces.adapter = adapter

        adapter.setOnclickItemListener { placeModel ->
            dialog = AddressDeleteDialog(requireActivity(), placeModel)
            dialog.show()
            dialog.setOnclickDeleteCallback { _placeModel ->
                viewModel.deletePlace(_placeModel)
                dialog.dismiss()
                mPlacesList.remove(placeModel)
                adapter.submitList(mPlacesList)
            }
        }

        adapter.setOnclickCallbackLocation {
            showToast("show show show", requireContext())

        }
    }

    private fun setupViewModel() {
        val dao = MyDataBase.getDatabase().placeModelDao()
        val repositoryImpl = AddressesRepositoryImpl(dao)
        viewModel = ViewModelProviders.of(
            this,
            AddressesViewModelFactory(repositoryImpl)
        ).get(AddressesViewModel::class.java)


        mPlacesList = ArrayList()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}