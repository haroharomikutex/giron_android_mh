package com.giron.android.view.loyalty

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.giron.android.R
import com.giron.android.databinding.FragmentLoyaltyHistoryBinding
import com.giron.android.extension.setOption
import com.giron.android.util.EndlessScrollListener
import com.giron.android.view.loyalty.adapter.LoyaltyHistoryAdapter
import com.giron.android.view.loyalty.listener.LoyaltyListener
import com.giron.android.view.loyalty.viewModel.LoyaltyRequestsViewModel
import com.giron.android.view.notifications.NoticeFragment
import com.giron.android.view.parts.listener.OnFragmentActiveListener
import com.giron.android.view.parts.listener.OnMenuSettingListener
import com.giron.android.view.parts.ActionBarMenuSetting

/**
 * LoyaltyHistoryFragment
 */
class LoyaltyHistoryFragment: Fragment(), OnMenuSettingListener, OnFragmentActiveListener,
    LoyaltyListener {
    override val actionBarMenuSetting: ActionBarMenuSetting
        get() = ActionBarMenuSetting()

    private lateinit var binding: FragmentLoyaltyHistoryBinding

    /**
     * onCreateView
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return View
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoyaltyHistoryBinding.inflate(inflater, container, false)
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

        val model = ViewModelProvider(this).get(LoyaltyRequestsViewModel::class.java)
        model.set(this, viewLifecycleOwner)
        binding.swipe.isRefreshing = true

        val viewAdapter = LoyaltyHistoryAdapter(model)
        val manager = LinearLayoutManager(requireContext())

        setOption()

        binding.list.apply {
            layoutManager = manager
            adapter = viewAdapter
        }

        // 無限スクロール
        binding.list.addOnScrollListener(object : EndlessScrollListener(manager) {
            override fun onLoadMore(current_page: Int) { // Load
                Log.d(TAG, "load more page $current_page")
                model.load()
            }
        })

        viewAdapter.model.items.observe(viewLifecycleOwner, {
            Log.d(NoticeFragment.TAG, "update list")
            viewAdapter.notifyDataSetChanged()
        })

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = true
            model.reload()
        }
    }

    /**
     * active
     */
    override fun active() {
        val act = activity as AppCompatActivity
        act.supportActionBar?.title = context?.getString(R.string.loyalty_request_history_title)
    }

    /**
     * finish
     */
    override fun finish() {
        binding.swipe.isRefreshing = false
    }

    /**
     * static
     */
    companion object {
        const val TAG = "LoyaltyHistoryFragment"
    }
}