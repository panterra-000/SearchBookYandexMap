package uz.rdo.projects.searchbookyandexmap.ui.fragments.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.rdo.projects.searchbookyandexmap.data.repository.MapRepository
import uz.rdo.projects.searchbookyandexmap.data.room.dao.PlaceMDao
import uz.rdo.projects.searchbookyandexmap.data.room.entity.PlaceModel
import uz.rdo.projects.searchbookyandexmap.utils.Coroutines

class MapRepositoryImpl(private val placeMDao: PlaceMDao) : MapRepository {
    override fun insertPlaceToDB(placeModel: PlaceModel): LiveData<Boolean> {
        val resultLiveData = MutableLiveData<Boolean>()
        Coroutines.ioThenMain(
            { placeMDao.insert(placeModel) },
            { id ->
                if (id != null) {
                    resultLiveData.value = id > 0
                }
            }
        )
        return resultLiveData
    }
}