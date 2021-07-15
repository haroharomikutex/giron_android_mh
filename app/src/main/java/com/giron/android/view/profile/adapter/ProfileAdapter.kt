@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.profile.adapter

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import com.giron.android.databinding.ItemUserProfileHeaderBinding
import com.giron.android.util.CircleOutlineProvider
import com.giron.android.util.RadiusOutlineProvider
import com.giron.android.view.parts.BaseViewAdapter
import com.giron.android.view.parts.BaseViewHolder
import com.giron.android.view.profile.listener.UserProfileListener
import com.giron.android.view.profile.parts.UserProfileSpannableFactory
import com.giron.android.view.profile.viewModel.ProfileHeaderViewModel
import com.giron.android.view.profile.viewModel.ProfileViewModel
import com.giron.android.view.parts.BaseViewModel

class ProfileAdapter(override val model: ProfileViewModel, private val listener: UserProfileListener): BaseViewAdapter(model) {
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
        val binding = ItemUserProfileHeaderBinding.inflate(inflater, parent, false)
        binding.lifecycleOwner = model.owner
        return ProfileHeaderHolder(binding, listener)
    }

    /**
     * ProfileHeaderHolder
     *
     * @param ItemUserProfileHeaderBinding
     */
    class ProfileHeaderHolder(val binding: ItemUserProfileHeaderBinding, private val listener: UserProfileListener): BaseViewHolder(binding.root) {
        override fun set(_model: BaseViewModel) {
            val model = _model as ProfileHeaderViewModel
            binding.user = model
            binding.back.outlineProvider = RadiusOutlineProvider(16F)
            binding.userImage.outlineProvider = CircleOutlineProvider()

            binding.profile.movementMethod = LinkMovementMethod.getInstance()
            binding.profile.setSpannableFactory(UserProfileSpannableFactory(binding.profile, listener, binding.root.context))
        }
    }
}