package com.giron.android.view.coin

import android.content.Intent
import android.net.Uri
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
import com.giron.android.databinding.FragmentListBinding
import com.giron.android.extension.mainActivity
import com.giron.android.extension.setOption
import com.giron.android.model.dao.TimeStampObj
import com.giron.android.model.net.realm.TimeStampObjApi
import com.giron.android.util.EndlessScrollListener
import com.giron.android.view.coin.adapter.AdvertisedCoinAdapter
import com.giron.android.view.coin.listener.AdvertisedCoinListener
import com.giron.android.view.coin.viewModel.AdvertisedCoinsViewModel
import com.giron.android.view.notifications.NoticeFragment
import com.giron.android.view.parts.listener.OnFragmentActiveListener
import com.giron.android.view.parts.listener.OnMenuSettingListener
import com.giron.android.view.parts.ActionBarMenuSetting

/**
 * LoyaltyHistoryFragment
 */
class AdvertisedCoinFragment: Fragment(), OnMenuSettingListener, OnFragmentActiveListener,
    AdvertisedCoinListener {
    override val actionBarMenuSetting: ActionBarMenuSetting
        get() = ActionBarMenuSetting()

    private lateinit var binding: FragmentListBinding

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
        binding = FragmentListBinding.inflate(inflater, container, false)
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

        val model = ViewModelProvider(this).get(AdvertisedCoinsViewModel::class.java)
        model.set(this, viewLifecycleOwner)
        binding.swipe.isRefreshing = true
        TimeStampObjApi().setData(TimeStampObj.Key.CoinByAdvertisement)

        val viewAdapter = AdvertisedCoinAdapter(model)
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

            mainActivity.refreshBatch()
        })

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = true
            model.reload()
            TimeStampObjApi().setData(TimeStampObj.Key.CoinByAdvertisement)
        }
    }

    /**
     * finish
     */
    override fun finish() {
        binding.swipe.isRefreshing = false
    }

    /**
     * active
     */
    override fun active() {
        val act = activity as AppCompatActivity
        act.supportActionBar?.title = context?.getString(R.string.advertisement_history)
    }

    /**
     * clickedAdvertisedCoin
     *
     * @param url
     */
    override fun clickedAdvertisedCoin(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    /**
     * static
     */
    companion object {
        const val TAG = "LoyaltyHistoryFragment"
    }
}