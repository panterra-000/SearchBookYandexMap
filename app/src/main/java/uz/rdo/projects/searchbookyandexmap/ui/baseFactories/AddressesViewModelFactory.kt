package uz.rdo.projects.searchbookyandexmap.ui.baseFactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.rdo.projects.searchbookyandexmap.data.repository.AddressesRepository
import uz.rdo.projects.searchbookyandexmap.ui.fragments.addresses.AddressesViewModel

class AddressesViewModelFactory(private val repository: AddressesRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddressesViewModel(repository) as T
    }
}