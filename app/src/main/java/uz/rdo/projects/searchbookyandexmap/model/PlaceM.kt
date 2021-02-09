package uz.rdo.projects.searchbookyandexmap.model

import com.yandex.mapkit.geometry.Point

data class PlaceM (
    var title: String,
    var subtitle: String,
    var distance: String,
    var point: Point?
)
