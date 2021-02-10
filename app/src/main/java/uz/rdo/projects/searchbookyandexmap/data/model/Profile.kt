package uz.rdo.projects.searchbookyandexmap.data.model

data class Profile(
    var id: Long,
    var name: String,
    var lastName: String,
    var age: Int,
    var address: String,
    var imageUrl: String
)