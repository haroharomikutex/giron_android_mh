package com.giron.android.view.parts.listener

import android.view.View

interface OnSearchViewListener {
    fun focusChange(view: View, b: Boolean)
    fun onQueryTextChange(text: String?): Boolean
    fun onQueryTextSubmit(text: String?): Boolean
    fun clickCloseButton()
}