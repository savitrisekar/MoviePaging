package com.example.movie

import android.view.View

/**
 * reference :
 * https://medium.com/@ekoo/mencoba-paging-3-0-f73221ae1d97
 * */

fun View.visibleWhen(show: Boolean) {
    if (show) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}