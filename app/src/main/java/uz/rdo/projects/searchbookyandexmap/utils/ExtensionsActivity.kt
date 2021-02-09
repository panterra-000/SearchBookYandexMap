package uz.rdo.projects.searchbookyandexmap.utils

import android.app.Activity
import android.widget.Toast


fun Activity.showToast(string: String){
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}