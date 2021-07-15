package com.giron.android.view.notifications

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
import com.giron.android.databinding.FragmentNoticesBinding
import com.giron.android.extension.mainActivity
import com.giron.android.extension.setOption
import com.giron.android.extension.transition
import com.giron.android.model.dao.TimeStampObj
import com.giron.android.model.dao.Transition
import com.giron.android.model.net.realm.TimeStampObjApi
import com.giron.android.util.EndlessScrollListener
import com.giron.android.view.notifications.adapter.NoticesAdapter
import com.giron.android.view.notifications.listener.NoticeListener
import com.giron.android.view.notifications.viewModel.NoticeViewModel
import com.giron.android.view.notifications.viewModel.NoticesViewModel
import com.giron.android.view.parts.listener.OnFragmentActiveListener
import com.giron.android.view.parts.listener.OnMenuSettingListener
import com.giron.android.view.parts.ActionBarMenuSetting

/**
 * NoticeFragment
 * 通知画面
 */
class NoticeFragment: Fragment(), OnMenuSettingListener, NoticeListener, OnFragmentActiveListener {
    private lateinit var binding: FragmentNoticesBinding

    /**
     * actionBarMenuSetting
     */
    override val actionBarMenuSetting: ActionBarMenuSetting
        get() = ActionBarMenuSetting()

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
        binding = FragmentNoticesBinding.inflate(inflater, container, false)
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

        val model = ViewModelProvider(this).get(NoticesViewModel::class.java)
        model.set(viewLifecycleOwner)
        val viewAdapter = NoticesAdapter(model, this)
        val manager = LinearLayoutManager(requireContext())

        setOption()

        if (model.items.value?.count() ?: 0 == 0) {
            model.getData()
            binding.swipe.isRefreshing = true

            TimeStampObjApi().setData(TimeStampObj.Key.NOTICE)
        }

        binding.list.apply {
            layoutManager = manager
            adapter = viewAdapter
        }

        // 無限スクロール
        binding.list.addOnScrollListener(object : EndlessScrollListener(manager) {
            override fun onLoadMore(current_page: Int) { // Load
                Log.d(TAG, "load more page $current_page")

                model.getData()
            }
        })

        viewAdapter.model.items.observe(viewLifecycleOwner, {
            Log.d(TAG, "update list")
            viewAdapter.notifyDataSetChanged()
            binding.swipe.isRefreshing = false

            mainActivity.refreshBatch()
        })

        binding.swipe.setOnRefreshListener {
            model.reset()
            TimeStampObjApi().setData(TimeStampObj.Key.NOTICE)
        }
    }

    /**
     * active
     */
    override fun active() {
        val act = activity as AppCompatActivity
        act.supportActionBar?.title = context?.getString(R.string.notice)
    }

    /**
     * onClickNotice
     *
     * @param model
     */
    override fun onClickNotice(model: NoticeViewModel) {
        transition(Transition.fromNotice(model))
    }

    /**
     * static
     */
    companion object {
        const val TAG = "NoticeFragment"
    }
}