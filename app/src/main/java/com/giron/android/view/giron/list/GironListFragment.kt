package com.giron.android.view.giron.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.giron.android.databinding.FragmentGironListBinding
import com.giron.android.extension.mainActivity
import com.giron.android.model.dao.TimeStampObj
import com.giron.android.model.net.realm.TimeStampObjApi
import com.giron.android.util.EndlessScrollListener
import com.giron.android.view.giron.list.adapter.GironsAdapter
import com.giron.android.view.giron.list.adapter.GironsListener
import com.giron.android.view.giron.list.viewModel.GironsViewModel
import com.giron.android.view.parts.ShareViewModel
import com.giron.android.view.parts.SharedViewModelStoreOwner

/**
 * GironListFragment
 * Giron一覧の各PagerViewの中身
 */
class GironListFragment : Fragment(),
    GironsListener, SharedViewModelStoreOwner {
    private val shareViewModel: ShareViewModel by lazy { ShareViewModel.get(this) }
    private var type: String = ""
    private var word: String = ""
    private var itemPos = -1
    private var topView = 0
    private lateinit var viewAdapter: GironsAdapter
    private lateinit var manager: LinearLayoutManager
    private lateinit var binding: FragmentGironListBinding

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)

        arguments?.let {
            type = it.getString("type").toString()
            word = it.getString("word").toString()
        }
    }

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
        binding = FragmentGironListBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * onViewCreated
     *
     * @param view
     * @param savedInstanceState
     */
    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val key = "${findNavController().backStack.count()}_$type"
        Log.d(TAG, "onViewCreated $key $word")

        val model = ViewModelProvider(this).get(key, GironsViewModel::class.java)
        if (model.set(type, word)) {
            binding.swipe.isRefreshing = true

            if (type == "new") {
                TimeStampObjApi().setData(TimeStampObj.Key.GIRON)
            }
        }
        viewAdapter = GironsAdapter(
            viewLifecycleOwner,
            model,
            this
        )
        manager = LinearLayoutManager(this@GironListFragment.context)

        binding.listView.apply {
            layoutManager = manager
            adapter = viewAdapter
        }

        viewAdapter.model.items.observe(viewLifecycleOwner, {
            Log.d(TAG, "update list")
            viewAdapter.notifyDataSetChanged()
            binding.swipe.isRefreshing = false

            mainActivity.refreshBatch()
        })

        binding.listView.addOnScrollListener(object : EndlessScrollListener(manager) {
            override fun onLoadMore(current_page: Int) { // Load
                Log.d(TAG, "load more page $current_page")
                viewAdapter.model.search()
            }
        })

        shareViewModel.searchByFragment.add {
            Log.d(TAG, "searchByFragment $it")
            word = it
        }

        binding.swipe.setOnRefreshListener {
            Log.d(TAG, "swipe")
            viewAdapter.model.reset()
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
     * onPause
     */
    override fun onPause() {
        Log.d(TAG, "onPause")

        itemPos = manager.findFirstVisibleItemPosition()

        val startView = binding.listView.getChildAt(0)
        topView = if (startView == null) 0 else startView.top - binding.listView.paddingTop

        super.onPause()
    }

    /**
     * onResume
     */
    override fun onResume() {
        Log.d(TAG, "onResume $itemPos $topView")

        if (itemPos != -1)
            manager.scrollToPositionWithOffset(itemPos, topView)

        super.onResume()

        viewAdapter.model.word.value = word
    }

    /**
     * onClickGirons
     *
     * @param id
     * @param num
     */
    override fun onClickGirons(id: Int, num: Int?) {
        Log.d(TAG, "click giron ID:$id")

        val action = GironTopFragmentDirections.actionGironTopFragmentToGironDetail()
        action.id = id
        num?.let {action.num = it}

        findNavController().navigate(action)
    }

    /**
     * static
     */
    companion object {
        const val TAG = "GironListFragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param type
         * @param word
         * @return A new instance of fragment GironDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(type: String, word: String) =
            GironListFragment().apply {
                arguments = Bundle().apply {
                    putString("type", type)
                    putString("word", word)
                }
            }
    }
}