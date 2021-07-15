package com.giron.android.view.giron.detail

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelStore
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.giron.android.databinding.FragmentEmotionBinding
import com.giron.android.extension.showError
import com.giron.android.model.net.CommentApiClient
import com.giron.android.model.net.GironApiClient
import com.giron.android.view.giron.detail.adapter.EmotionsAdapter
import com.giron.android.view.giron.detail.listener.EmotionButtonsListener
import com.giron.android.view.giron.detail.viewModel.EmotionType
import com.giron.android.view.parts.ShareViewModel
import com.giron.android.view.parts.SharedViewModelStoreOwner
import com.google.android.flexbox.*

/**
 * EmotionsDialogFragment
 */
class EmotionsDialogFragment: DialogFragment(), EmotionButtonsListener, SharedViewModelStoreOwner {
    private val shareViewModel: ShareViewModel by lazy { ShareViewModel.get(this) }
    private val args : EmotionsDialogFragmentArgs by navArgs()

    private lateinit var viewAdapter: EmotionsAdapter
    private lateinit var binding: FragmentEmotionBinding

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
    ): View {
        binding = FragmentEmotionBinding.inflate(inflater, container, false)
        viewAdapter = EmotionsAdapter(viewLifecycleOwner, EmotionType.createViewModelList(), this)
        return binding.root
    }

    /**
     * onViewCreated
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // flexboxlayout
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        flexboxLayoutManager.alignItems = AlignItems.CENTER

        binding.list.apply {
            adapter = viewAdapter
            layoutManager = flexboxLayoutManager
        }

        viewAdapter.model.items.observe(viewLifecycleOwner, {
            Log.d(TAG, "update list")
            viewAdapter.notifyDataSetChanged()
        })

        // set dialog position
        dialog?.window?.attributes?.let {
            it.gravity = Gravity.TOP
            it.y = args.y
            dialog?.window?.attributes = it
        }
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
     * addEmotion
     *
     * @param type
     */
    override fun addEmotion(type: EmotionType) {
        if (args.type == "giron") {
            GironApiClient().emotion(args.modelId, type, { entity ->
                shareViewModel.addEmotionByFragment?.invoke(entity)
                findNavController().navigateUp()
            }, { err ->
                context?.showError(err)
            })
        } else if (args.type == "comment") {
            CommentApiClient().emotion(args.modelId, type, { entity ->
                shareViewModel.addEmotionByFragment?.invoke(entity)
                findNavController().navigateUp()
            }, { err ->
                context?.showError(err)
            })
        }
    }

    companion object {
        const val TAG = "EmotionsDialogFragment"
    }
}