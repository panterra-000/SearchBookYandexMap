package uz.rdo.projects.searchbookyandexmap.ui.fragments.map

import androidx.lifecycle.LiveData
import uz.rdo.projects.searchbookyandexmap.data.repository.MapRepository
import uz.rdo.projects.searchbookyandexmap.data.room.entity.PlaceModel

class MapRepositoryImpl : MapRepository{
    override fun insertPlaceToDB(placeModel: PlaceModel): LiveData<Boolean> {
        TODO("Not yet implemented")
    }
}