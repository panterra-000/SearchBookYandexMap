package uz.rdo.projects.searchbookyandexmap.ui.fragments.profile

import androidx.lifecycle.LiveData
import uz.rdo.projects.searchbookyandexmap.data.model.Profile
import uz.rdo.projects.searchbookyandexmap.data.repository.ProfileRepository

class ProfileRepositoryImpl:ProfileRepository {
    override fun getProfileInformation(id: Long): LiveData<Profile> {
        TODO("Not yet implemented")
    }
}