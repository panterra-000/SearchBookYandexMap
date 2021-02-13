package uz.rdo.projects.searchbookyandexmap.utils

import android.view.View
import android.view.ViewGroup.MarginLayoutParams


fun View.changeFormer(){
    if (this.layoutParams is MarginLayoutParams) {
        val p = this.layoutParams as MarginLayoutParams
        p.setMargins(0, 0, 0, 25)
        this.requestLayout()
    }
}

fun View.resumeFormer(){
    if (this.layoutParams is MarginLayoutParams) {
        val p = this.layoutParams as MarginLayoutParams
        p.setMargins(0, 0, 0, 7)
        this.requestLayout()
    }
}

