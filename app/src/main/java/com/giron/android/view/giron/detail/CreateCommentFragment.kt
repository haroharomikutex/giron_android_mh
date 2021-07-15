@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.giron.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelStore
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.giron.android.databinding.FragmentCreateCommentBinding
import com.giron.android.extension.closeKeyboard
import com.giron.android.extension.setOption
import com.giron.android.extension.showError
import com.giron.android.model.net.realm.WritingCommentObjApi
import com.giron.android.model.net.GironApiClient
import com.giron.android.view.giron.detail.CreateCommentFragmentArgs
import com.giron.android.view.parts.listener.OnAddPressedListener
import com.giron.android.view.parts.listener.OnBackPressedListener
import com.giron.android.view.parts.listener.OnMenuSettingListener
import com.giron.android.view.parts.ActionBarMenuSetting
import com.giron.android.view.giron.detail.viewModel.CreateCommentViewModel
import com.giron.android.view.parts.ShareViewModel
import com.giron.android.view.parts.SharedViewModelStoreOwner

/**
 * CreateCommentFragment
 * コメント書き込み画面
 */
class CreateCommentFragment: Fragment(), SharedViewModelStoreOwner, OnBackPressedListener,
    OnAddPressedListener, OnMenuSettingListener {
    private val shareViewModel: ShareViewModel by lazy { ShareViewModel.get(this) }
    private val model: CreateCommentViewModel by viewModels()
    private val args: CreateCommentFragmentArgs by navArgs()

    /**
     * ActionBarMenuSetting
     * メニュー設定
     */
    override val actionBarMenuSetting: ActionBarMenuSetting
        get() = ActionBarMenuSetting(navVisible = false, addVisible = true)

    /**
     * onCreateView
     *
     * @param LayoutInflater
     * @param ViewGroup?
     * @param Bundle?
     *
     * @return View?
     */
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")

        val binding = FragmentCreateCommentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.comment = model

        return binding.root
    }

    /**
     * onViewCreated
     *
     * @param View
     * @param Bundle?
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        model.id = args.id
        model.setComment()

        setOption()

        super.onViewCreated(view, savedInstanceState)
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

    /**
     * onBackPressed
     *
     * @return Boolean
     */
    override fun onBackPressed(): Boolean {
        // Retract the keyboard
        closeKeyboard()

        // Save incomplete comments
        model.comment.value?.let {
            WritingCommentObjApi().setData(args.id, it)
        }

        return true
    }

    /**
     * add
     */
    override fun add() {
        model.comment.value?.let { comment ->
            GironApiClient().createComment(args.id, comment, {

                shareViewModel.writeCommentByFragment?.invoke(it.num)

                closeKeyboard()

                model.comment.value = ""

                findNavController().navigateUp()
            }, {
                context?.showError(it)
            })

        }
    }

    /**
     * static
     */
    companion object {
        const val TAG = "CreateCommentFragment"
    }
}