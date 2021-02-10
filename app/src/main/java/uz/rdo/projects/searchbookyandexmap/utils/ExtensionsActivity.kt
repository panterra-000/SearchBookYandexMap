package uz.rdo.projects.searchbookyandexmap.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast


fun showToast(string: String, context: Context) {
    Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
}