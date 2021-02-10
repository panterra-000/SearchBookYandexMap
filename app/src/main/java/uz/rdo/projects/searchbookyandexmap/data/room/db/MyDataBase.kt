package uz.rdo.projects.searchbookyandexmap.data.room.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.rdo.projects.searchbookyandexmap.app.App
import uz.rdo.projects.searchbookyandexmap.data.room.dao.PlaceMDao
import uz.rdo.projects.searchbookyandexmap.data.room.entity.PlaceModel

@Database(entities = [PlaceModel::class], version = 1)
abstract class MyDataBase : RoomDatabase() {
    abstract fun placeModelDao(): PlaceMDao

    companion object {
        @Volatile
        private var INSTANCE: MyDataBase? = null
        fun getDatabase(): MyDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    App.instance.applicationContext,
                    MyDataBase::class.java,
                    "app_database"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}