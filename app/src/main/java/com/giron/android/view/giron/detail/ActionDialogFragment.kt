package com.giron.android.view.giron.detail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelStore
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.giron.android.databinding.FragmentCommentActionBinding
import com.giron.android.extension.navigateUp
import com.giron.android.extension.setOnSafeClickListener
import com.giron.android.extension.showError
import com.giron.android.model.net.CommentApiClient
import com.giron.android.model.net.realm.UserObjApi
import com.giron.android.view.giron.detail.adapter.EmotionsAdapter
import com.giron.android.view.giron.detail.adapter.RewordsAdapter
import com.giron.android.view.giron.detail.listener.EmotionButtonsListener
import com.giron.android.view.giron.detail.listener.RewordsListener
import com.giron.android.view.giron.detail.viewModel.EmotionType
import com.giron.android.view.giron.detail.viewModel.RewordsViewModel
import com.giron.android.view.parts.ShareViewModel
import com.giron.android.view.parts.SharedViewModelStoreOwner
import com.google.android.flexbox.*


/**
 * ActionDialogFragment
 */
class ActionDialogFragment: DialogFragment(), RewordsListener, EmotionButtonsListener,
    SharedViewModelStoreOwner {
    private val shareViewModel: ShareViewModel by lazy { ShareViewModel.get(this) }
    private val args: ActionDialogFragmentArgs by navArgs()
    private lateinit var rewordViewAdapter: RewordsAdapter
    private lateinit var emotionViewAdapter: EmotionsAdapter
    private lateinit var binding: FragmentCommentActionBinding

    /**
     * onCreateView
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView")

        rewordViewAdapter = RewordsAdapter(viewLifecycleOwner, RewordsViewModel(this), this)
        emotionViewAdapter = EmotionsAdapter(viewLifecycleOwner, EmotionType.createViewModelList(), this)
        binding = FragmentCommentActionBinding.inflate(inflater, container, false)
        UserObjApi().getData()?.let { user ->
            binding.myUser = user
        }
        return binding.root
    }

    /**
     * onViewCreated
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /**
         * Reword
         */
        val flexLayoutManager = FlexboxLayoutManager(context)
        flexLayoutManager.flexDirection = FlexDirection.ROW
        flexLayoutManager.flexWrap = FlexWrap.WRAP
        flexLayoutManager.justifyContent = JustifyContent.SPACE_AROUND
        flexLayoutManager.alignItems = AlignItems.CENTER

        binding.rewords.apply {
            adapter = rewordViewAdapter
            layoutManager = flexLayoutManager
        }

        rewordViewAdapter.model.items.observe(viewLifecycleOwner, {
            Log.d(TAG, "update list")
            rewordViewAdapter.notifyDataSetChanged()
        })

        /**
         * Emotion
         */
        val flexboxLayoutManager2 = FlexboxLayoutManager(context)
        flexboxLayoutManager2.flexDirection = FlexDirection.ROW
        flexboxLayoutManager2.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager2.justifyContent = JustifyContent.FLEX_START
        flexboxLayoutManager2.alignItems = AlignItems.CENTER

        binding.emotions.apply {
            adapter = emotionViewAdapter
            layoutManager = flexboxLayoutManager2
        }

        emotionViewAdapter.model.items.observe(viewLifecycleOwner, {
            Log.d(TAG, "update list")
            emotionViewAdapter.notifyDataSetChanged()
        })

        // set dialog position
        dialog?.window?.attributes?.let {
            it.gravity = Gravity.BOTTOM
            dialog?.window?.attributes = it
        }

        binding.copyComment.setOnSafeClickListener {
            shareViewModel.copyByFragment?.invoke()
            navigateUp()
        }

        binding.reply.setOnSafeClickListener {
            navigateUp()
            Handler(Looper.getMainLooper()).postDelayed({
                shareViewModel.replyByFragment?.invoke()
            }, 1000)
        }
    }

    /**
     * getViewModelStore
     *
     * @return ViewModelStore
     */
    override fun getViewModelStore(): ViewModelStore {
        return super<SharedViewModelStoreOwner>.getViewModelStore()
    }

    /**
     * 各リワードボタンがタップされたときの処理
     *
     * @param rewordId
     */
    override fun reword(rewordId: Int) {
        CommentApiClient().reword(args.id, rewordId, {coinResponse ->
            UserObjApi().setCoin(coinResponse.userCoin)

            shareViewModel.rewordByFragment?.invoke(coinResponse.targetCoin?: 0)
            findNavController().navigateUp()
        }, {
            context?.showError(it)
        })
    }

    /**
     * 各エモーションボタンがタップされたときの処理
     */
    override fun addEmotion(type: EmotionType) {
        CommentApiClient().emotion(args.id, type, { entity ->
            shareViewModel.addEmotionByFragment?.invoke(entity)
            findNavController().navigateUp()
        }, {})
    }

    companion object {
        private const val TAG = "ActionDialogFragment"
    }
}