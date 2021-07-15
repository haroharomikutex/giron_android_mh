@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.information.adapter

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import com.giron.android.databinding.ItemInformationBinding
import com.giron.android.view.information.viewModel.InformationViewModel
import com.giron.android.view.information.viewModel.InformationsViewModel
import com.giron.android.view.parts.BaseViewAdapter
import com.giron.android.view.parts.BaseViewHolder
import com.giron.android.util.GironSpannableFactory
import com.giron.android.view.parts.BaseViewModel

/**
 * InformationAdapter
 *
 * @param InformationsViewModel
 */
class InformationAdapter(override val model: InformationsViewModel): BaseViewAdapter(model) {
    /**
     * onCreateViewHolder
     *
     * @param ViewGroup
     * @param Int
     *
     * @return BaseViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemInformationBinding.inflate(inflater, parent, false)
        binding.lifecycleOwner = model.owner
        return InformationViewHolder(binding)
    }

    /**
     * InformationViewHolder
     *
     * @param ItemInformationBinding
     */
    class InformationViewHolder(val binding: ItemInformationBinding): BaseViewHolder(binding.root) {
        /**
         * set
         *
         * @param BaseViewModel
         */
        override fun set(_model: BaseViewModel) {
            val model = _model as InformationViewModel
            binding.information = model

            binding.message.movementMethod = LinkMovementMethod.getInstance()
            binding.message.setSpannableFactory(GironSpannableFactory(binding.message, binding.root.context, { id, num->
                model.listener.touchGiron(id, num)
            }, {
                model.listener.touchTag(it)
            }))
        }
    }
}