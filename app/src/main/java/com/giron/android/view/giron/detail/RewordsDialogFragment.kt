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
import com.giron.android.databinding.FragmentRewordBinding
import com.giron.android.extension.showError
import com.giron.android.model.net.realm.UserObjApi
import com.giron.android.model.net.GironApiClient
import com.giron.android.view.giron.detail.adapter.RewordsAdapter
import com.giron.android.view.giron.detail.listener.RewordsListener
import com.giron.android.view.giron.detail.viewModel.RewordsViewModel
import com.giron.android.view.parts.ShareViewModel
import com.giron.android.view.parts.SharedViewModelStoreOwner
import com.google.android.flexbox.*

/**
 * RewordsDialogFragment
 */
class RewordsDialogFragment : DialogFragment(), RewordsListener, SharedViewModelStoreOwner {
    private val shareViewModel: ShareViewModel by lazy { ShareViewModel.get(this) }
    private val args : RewordsDialogFragmentArgs by navArgs()
    private lateinit var viewAdapter: RewordsAdapter
    private lateinit var binding: FragmentRewordBinding

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

        viewAdapter = RewordsAdapter(viewLifecycleOwner, RewordsViewModel(this), this)
        binding = FragmentRewordBinding.inflate(inflater, container, false)
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
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.SPACE_AROUND
        flexboxLayoutManager.alignItems = AlignItems.CENTER

        // 3つ表示
        binding.list.apply {
            adapter = viewAdapter
            //layoutManager = GridLayoutManager(context, 3)
            layoutManager = flexboxLayoutManager
        }

        viewAdapter.model.items.observe(viewLifecycleOwner, {
            Log.d(TAG, "update list")
            viewAdapter.notifyDataSetChanged()
        })

        // set dialog position
        dialog?.window?.attributes?.let {
            it.gravity = Gravity.BOTTOM
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
     * reword
     *
     * @param rewordId
     */
    override fun reword(rewordId: Int) {
        GironApiClient().reword(args.id, rewordId, {response ->
            UserObjApi().setCoin(response.userCoin)

            shareViewModel.rewordByFragment?.invoke(response.targetCoin ?: 0)
            findNavController().navigateUp()
        },{
            context?.showError(it)
        })
    }

    /**
     * static
     */
    companion object {
        private const val TAG = "RewordsDialogFragment"
    }
}