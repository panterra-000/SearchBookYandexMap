package uz.rdo.projects.searchbookyandexmap.ui.fragments.addresses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.rdo.projects.searchbookyandexmap.data.repository.AddressesRepository
import uz.rdo.projects.searchbookyandexmap.data.room.dao.PlaceMDao
import uz.rdo.projects.searchbookyandexmap.data.room.entity.PlaceModel
import uz.rdo.projects.searchbookyandexmap.utils.Coroutines

class AddressesRepositoryImpl(private val placeMDao: PlaceMDao) : AddressesRepository {

    override fun getAllPlacesFromDB(): LiveData<List<PlaceModel>> {
        val resultLiveData = MutableLiveData<List<PlaceModel>>()
        Coroutines.ioThenMain(
            { placeMDao.getAllPlaceModels() },
            { placesList ->
                if (placesList != null) {
                    resultLiveData.value = placesList
                }
            }
        )
        return resultLiveData

    }

    override fun deleteAllPlacesDb(): LiveData<Int> {
        val resultData = MutableLiveData<Int>()
        Coroutines.ioThenMain(
            { placeMDao.deleteAllPlaces() },
            {
                resultData.value = it
            }
        )
        return resultData
    }

    override fun deletePlace(placeModel: PlaceModel): LiveData<Boolean> {
        val resultData = MutableLiveData<Boolean>()
        Coroutines.ioThenMain(
            { placeMDao.delete(placeModel) },
            { id ->
                if (id != null) {
                    resultData.value = id > 0
                }
            }
        )
        return resultData
    }

}