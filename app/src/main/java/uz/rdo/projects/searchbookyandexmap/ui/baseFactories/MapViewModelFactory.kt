package uz.rdo.projects.searchbookyandexmap.ui.baseFactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.rdo.projects.searchbookyandexmap.data.repository.MapRepository
import uz.rdo.projects.searchbookyandexmap.ui.fragments.map.MapViewModel

class MapViewModelFactory(private val repository: MapRepository):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MapViewModel(repository) as T
    }
}