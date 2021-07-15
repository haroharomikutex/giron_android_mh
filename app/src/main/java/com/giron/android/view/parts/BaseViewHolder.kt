package com.giron.android.view.parts

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    open fun set(_model: BaseViewModel) {}
}