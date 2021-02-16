package uz.rdo.projects.searchbookyandexmap.localdata

import android.content.Context
import android.content.SharedPreferences
import uz.rdo.projects.searchbookyandexmap.utils.DoublePreference

class LocalStorage(context: Context) {


    private val pref: SharedPreferences =
        context.getSharedPreferences("LocalStorage", Context.MODE_PRIVATE)

    var currentLat by DoublePreference(pref, 0.0)
    var currentLong by DoublePreference(pref, 0.0)


}