package uz.rdo.projects.searchbookyandexmap.data.repository

import androidx.lifecycle.LiveData
import uz.rdo.projects.searchbookyandexmap.data.model.Profile
import uz.rdo.projects.searchbookyandexmap.data.room.entity.PlaceModel


interface MapRepository {
    fun insertPlaceToDB(placeModel: PlaceModel): LiveData<Boolean>
}

interface AddressesRepository {
    fun getAllPlacesFromDB(): LiveData<List<PlaceModel>>
    fun deletePlaceFromDB(placeModel: PlaceModel): LiveData<Boolean>
}

interface ProfileRepository {
    fun getProfileInformation(id: Long): LiveData<Profile>
}