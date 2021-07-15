@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.giron.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.giron.android.R
import com.giron.android.databinding.ItemGironGironsBinding
import com.giron.android.util.CircleOutlineProvider
import com.giron.android.view.giron.list.viewModel.GironListItemViewModel
import com.giron.android.view.giron.list.viewModel.GironsViewModel


interface GironsListener {
    fun onClickGirons(id: Int, num: Int? = null)
}

/**
 * GironsAdapter
 *
 * @param LifecycleOwner
 * @param GironsViewModel
 * @param GironsListener
 */
class GironsAdapter(val owner: LifecycleOwner, val model: GironsViewModel, private val listener: GironsListener) : RecyclerView.Adapter<GironsAdapter.GironsViewHolder>() {
    class GironsViewHolder(val binding: ItemGironGironsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return model.items.value?.count() ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GironsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemGironGironsBinding>(inflater, R.layout.item_giron_girons, parent, false)
        binding.lifecycleOwner = owner
        return GironsViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: GironsViewHolder, position: Int) {
        val girons = model.items.value ?: return

        if (girons.count() < position) return
        val model = girons[position] as GironListItemViewModel
        holder.binding.image.outlineProvider = CircleOutlineProvider()
        holder.binding.giron = model

        holder.itemView.setOnClickListener {
            listener.onClickGirons(model.giron.id)
        }
    }
}