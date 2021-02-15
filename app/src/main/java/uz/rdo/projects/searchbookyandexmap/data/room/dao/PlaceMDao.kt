package uz.rdo.projects.searchbookyandexmap.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import uz.rdo.projects.searchbookyandexmap.data.room.entity.PlaceModel

@Dao
interface PlaceMDao : BaseDao<PlaceModel> {
    @Query("SELECT * FROM  placemodel")
    fun getAllPlaceModels(): List<PlaceModel>

    @Query("DELETE from placemodel")
    fun deleteAllPlaces(): Int
}