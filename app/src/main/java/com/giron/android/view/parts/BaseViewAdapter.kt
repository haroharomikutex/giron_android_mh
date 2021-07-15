package com.giron.android.view.parts

import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView

open class BaseViewAdapter(open val model: BaseViewModelList): RecyclerView.Adapter<BaseViewHolder>() {
    override fun getItemCount(): Int {
        return model.items.value?.count() ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        val items = model.items.value ?: return 0
        val model = items[position]
        return model.modelType.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(parent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val models = model.items.value ?: return

        if (models.count() < position) return
        holder.set(models[position])
    }
}