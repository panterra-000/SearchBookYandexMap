package uz.rdo.projects.searchbookyandexmap.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class PlaceModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var subtitle: String,
    var distance: String,
    var allReview: Int?,
    var score: Float?,
    var longitude: Double,
    var latitude: Double
) : Serializable