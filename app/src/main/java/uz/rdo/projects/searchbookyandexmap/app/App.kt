package uz.rdo.projects.searchbookyandexmap.app

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import uz.rdo.projects.searchbookyandexmap.consts.KEY_YANDEX

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        MapKitFactory.setApiKey(KEY_YANDEX)
    }

    companion object {
        lateinit var instance: App
    }
}