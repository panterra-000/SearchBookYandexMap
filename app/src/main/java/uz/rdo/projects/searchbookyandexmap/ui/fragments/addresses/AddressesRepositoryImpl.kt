package uz.rdo.projects.searchbookyandexmap.ui.fragments.addresses

import androidx.lifecycle.LiveData
import uz.rdo.projects.searchbookyandexmap.data.repository.AddressesRepository
import uz.rdo.projects.searchbookyandexmap.data.room.entity.PlaceModel

class AddressesRepositoryImpl:AddressesRepository {

    override fun getAllPlacesFromDB(): LiveData<List<PlaceModel>> {
        TODO("Not yet implemented")
    }

    override fun deletePlaceFromDB(placeModel: PlaceModel): LiveData<Boolean> {
        TODO("Not yet implemented")
    }
}