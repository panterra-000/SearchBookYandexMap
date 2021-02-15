package uz.rdo.projects.searchbookyandexmap.ui.fragments.addresses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import uz.rdo.projects.searchbookyandexmap.data.repository.AddressesRepository
import uz.rdo.projects.searchbookyandexmap.data.room.entity.PlaceModel
import uz.rdo.projects.searchbookyandexmap.utils.addSourceDisposable

class AddressesViewModel(private val addressesRepository: AddressesRepository) : ViewModel() {

    private val _resultPlacesList = MediatorLiveData<List<PlaceModel>>()
    val resultPlacesList: LiveData<List<PlaceModel>> get() = _resultPlacesList

    private val _resultDeleteAllPlaces = MediatorLiveData<Int>()
    val resultDeleteAllPlaces: LiveData<Int> get() = _resultDeleteAllPlaces


    fun getAllPlacesList() {
        _resultPlacesList.addSourceDisposable(addressesRepository.getAllPlacesFromDB()) { allPlacesList ->
            _resultPlacesList.value = allPlacesList
        }
    }

    fun deleteAllPlaceList() {
        _resultDeleteAllPlaces.addSourceDisposable(addressesRepository.deleteAllPlacesDb()) {
            _resultDeleteAllPlaces.value = it
        }
    }
}