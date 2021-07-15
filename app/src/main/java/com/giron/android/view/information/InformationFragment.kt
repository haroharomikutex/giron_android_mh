package com.giron.android.view.information

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.giron.android.R
import com.giron.android.databinding.FragmentListBinding
import com.giron.android.extension.mainActivity
import com.giron.android.extension.setOption
import com.giron.android.model.dao.TimeStampObj
import com.giron.android.model.net.realm.TimeStampObjApi
import com.giron.android.util.EndlessScrollListener
import com.giron.android.view.information.adapter.InformationAdapter
import com.giron.android.view.information.listener.InformationListener
import com.giron.android.view.information.viewModel.InformationsViewModel
import com.giron.android.view.notifications.NoticeFragment
import com.giron.android.view.parts.listener.OnFragmentActiveListener
import com.giron.android.view.parts.listener.OnMenuSettingListener
import com.giron.android.view.parts.ActionBarMenuSetting

/**
 * InformationFragment
 */
class InformationFragment:
    Fragment(),
    OnMenuSettingListener,
    OnFragmentActiveListener,
    InformationListener
{
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

        val model = ViewModelProvider(this).get(InformationsViewModel::class.java)
        model.set(viewLifecycleOwner, this)
        binding.swipe.isRefreshing = true
        TimeStampObjApi().setData(TimeStampObj.Key.INFORMATION)

        val viewAdapter = InformationAdapter(model)
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
            TimeStampObjApi().setData(TimeStampObj.Key.INFORMATION)
        }
    }

    /**
     * touchGiron
     *
     * @param id Giron ID
     * @param num Comment Number
     */
    override fun touchGiron(id: Int, num: Int?) {
        val action = InformationFragmentDirections.actionInformationFragmentToGironDetail()
        action.id = id
        num?.let {action.num = it}

        findNavController().navigate(action)
    }

    /**
     * touchUrl
     *
     * @param url
     * @param required
     */
    override fun touchUrl(url: String, required: Boolean) {
        val action = InformationFragmentDirections.actionInformationFragmentToWebFragment()
        action.url = url
        action.required = required

        findNavController().navigate(action)
    }

    /**
     * touchTag
     *
     * @param tag
     */
    override fun touchTag(tag: String) {
        val action = InformationFragmentDirections.actionInformationFragmentToGironTopFragment()
        action.word = tag

        findNavController().navigate(action)
    }

    /**
     * active
     */
    override fun active() {
        val act = activity as AppCompatActivity
        act.supportActionBar?.title = context?.getString(R.string.information)
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
        const val TAG = "InformationFragment"
    }
}