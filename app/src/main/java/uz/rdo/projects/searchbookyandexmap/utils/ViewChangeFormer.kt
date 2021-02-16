package uz.rdo.projects.searchbookyandexmap.utils

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import uz.rdo.projects.searchbookyandexmap.R


fun View.changeFormer() {
    if (this.layoutParams is MarginLayoutParams) {
        val p = this.layoutParams as MarginLayoutParams
        p.setMargins(0, 0, 0, 25)
        this.requestLayout()
    }
}

fun View.resumeFormer() {
    if (this.layoutParams is MarginLayoutParams) {
        val p = this.layoutParams as MarginLayoutParams
        p.setMargins(0, 0, 0, 7)
        this.requestLayout()
    }
}

fun View.changeFormerShade() {
    if (this.layoutParams is ViewGroup.LayoutParams) {
        val p = this.layoutParams as ViewGroup.LayoutParams
        p.width = 40
        p.height = 10
        this.requestLayout()
    }
}

fun View.resumeFormerShade() {
    if (this.layoutParams is ViewGroup.LayoutParams) {
        val p = this.layoutParams as ViewGroup.LayoutParams
        p.width = 64
        p.height = 20
        this.requestLayout()
    }
}

