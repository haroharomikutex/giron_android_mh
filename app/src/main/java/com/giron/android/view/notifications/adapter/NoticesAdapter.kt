@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.notifications.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.giron.android.databinding.ItemNoticeBinding
import com.giron.android.util.CircleOutlineProvider
import com.giron.android.view.notifications.listener.NoticeListener
import com.giron.android.view.notifications.viewModel.NoticeViewModel
import com.giron.android.view.notifications.viewModel.NoticesViewModel
import com.giron.android.view.parts.BaseViewAdapter
import com.giron.android.view.parts.BaseViewHolder
import com.giron.android.view.parts.BaseViewModel

/**
 * NoticesViewModel
 *
 * @param NoticesViewModel
 * @param NoticeListener
 */
class NoticesAdapter(override val model: NoticesViewModel, val listener: NoticeListener) : BaseViewAdapter(model) {

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
        val binding = ItemNoticeBinding.inflate(inflater, parent, false)
        binding.lifecycleOwner = model.owner
        return NoticesViewHolder(binding, listener)
    }

    /**
     * NoticesViewHolder
     *
     * @param ItemNoticeBinding
     * @param NoticeListener
     */
    class NoticesViewHolder(val binding: ItemNoticeBinding, val listener: NoticeListener) : BaseViewHolder(binding.root) {
        override fun set(_model: BaseViewModel) {
            val model = _model as NoticeViewModel
            binding.notice = model
            binding.user.outlineProvider = CircleOutlineProvider()

            binding.main.setOnClickListener {
                listener.onClickNotice(model)
            }
        }
    }
}