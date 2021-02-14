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

    private val _resultDelPlacesList = MediatorLiveData<Boolean>()
    val resultDelPlacesList: LiveData<Boolean> get() = _resultDelPlacesList


    fun getAllPlacesList() {
        _resultPlacesList.addSourceDisposable(addressesRepository.getAllPlacesFromDB()) { allPlacesList ->
            _resultPlacesList.value = allPlacesList
        }
    }

  /*  fun deleteAllPlaceList() {
        _resultDelPlacesList.addSourceDisposable(addressesRepository.deleteAllPlacesDb()) { isDeleted ->
            _resultDelPlacesList.value = isDeleted
        }
    }*/
}