@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.editProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.navigation.fragment.findNavController
import com.giron.android.R
import com.giron.android.databinding.FragmentEditProfileBinding
import com.giron.android.extension.closeKeyboard
import com.giron.android.extension.setOption
import com.giron.android.extension.showError
import com.giron.android.model.net.UserApiClient
import com.giron.android.view.editProfile.viewModel.EditProfileViewModel
import com.giron.android.view.parts.listener.OnBackPressedListener
import com.giron.android.view.parts.listener.OnFragmentActiveListener
import com.giron.android.view.parts.listener.OnMenuSettingListener
import com.giron.android.view.parts.listener.OnUpdatePressedListener
import com.giron.android.view.parts.ActionBarMenuSetting
import com.giron.android.view.parts.ShareViewModel
import com.giron.android.view.parts.SharedViewModelStoreOwner

/**
 * EditProfileFragment
 */
class EditProfileFragment: Fragment(),
    SharedViewModelStoreOwner,
    OnFragmentActiveListener,
    OnBackPressedListener,
    OnMenuSettingListener,
    OnUpdatePressedListener
{
    private val shareViewModel: ShareViewModel by lazy { ShareViewModel.get(this)}
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var model: EditProfileViewModel

    /**
     * actionBarMenuSetting
     */
    override val actionBarMenuSetting: ActionBarMenuSetting
        get() = ActionBarMenuSetting(updateVisible = true)

    /**
     * onCreateView
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return View?
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    /**
     * onViewCreated
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProvider(this).get(EditProfileViewModel::class.java)
        model.set(viewLifecycleOwner)
        binding.user = model

        setOption()
    }

    /**
     * update
     */
    override fun update() {
        val name = model.username.value ?: ""
        val profile = model.profile.value ?: ""
        UserApiClient().editProfile(name, profile, {
            model.isEdited.value = false

            shareViewModel.editProfileByFragment?.invoke(it)

            closeKeyboard()
            findNavController().navigateUp()
        }){
            context?.showError(it)
        }
    }

    /**
     * onBackPressed
     *
     * @return Boolean
     */
    override fun onBackPressed(): Boolean {
        closeKeyboard()

        // 未編集の場合は終了
        val isEdited = model.isEdited.value ?: false
        if (!isEdited) return true

        context?.let {
            AlertDialog
                .Builder(it)
                .setMessage(it.getString(R.string.alert_editing))
                .setPositiveButton("OK") { _, _ ->
                    model.isEdited.value = false
                    findNavController().navigateUp()
                }.setNegativeButton("Cancel", null)
                .show()
            return false
        }

        return true
    }

    /**
     * active
     */
    override fun active() {
        val act = activity as AppCompatActivity
        act.supportActionBar?.title = context?.getString(R.string.edit_profile_buttom_label)
    }

    /**
     * getViewModelStore
     * 共通のViewModelを使うために必要
     *
     * @return ViewModelStore
     */
    override fun getViewModelStore(): ViewModelStore {
        return super<SharedViewModelStoreOwner>.getViewModelStore()
    }
}