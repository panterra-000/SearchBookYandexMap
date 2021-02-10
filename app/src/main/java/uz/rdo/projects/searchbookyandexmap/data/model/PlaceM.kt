package uz.rdo.projects.searchbookyandexmap.data.model

import com.yandex.mapkit.geometry.Point

data class PlaceM (
    var title: String,
    var subtitle: String,
    var distance: String,
    var point: Point?
)
