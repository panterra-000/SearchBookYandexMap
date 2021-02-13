package uz.rdo.projects.searchbookyandexmap.ui.fragments.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import uz.rdo.projects.searchbookyandexmap.data.repository.MapRepository
import uz.rdo.projects.searchbookyandexmap.data.room.entity.PlaceModel
import uz.rdo.projects.searchbookyandexmap.utils.addSourceDisposable

class MapViewModel(private val mapRepository: MapRepository) : ViewModel() {

    private val _resultLiveData = MediatorLiveData<Boolean>()
    val resultLiveData: LiveData<Boolean> get() = _resultLiveData

    fun addPlaceModelToDB(placeModel: PlaceModel) {
        _resultLiveData.addSourceDisposable(mapRepository.insertPlaceToDB(placeModel)) { isAdded ->
            _resultLiveData.value = isAdded
        }
    }
}




