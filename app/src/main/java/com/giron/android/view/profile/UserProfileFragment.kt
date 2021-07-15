package com.giron.android.view.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.giron.android.GironsNavigationDirections
import com.giron.android.databinding.FragmentProfileBinding
import com.giron.android.extension.navigate
import com.giron.android.extension.setOption
import com.giron.android.view.parts.listener.OnMenuSettingListener
import com.giron.android.view.profile.adapter.ProfileAdapter
import com.giron.android.view.profile.listener.UserProfileListener
import com.giron.android.view.profile.viewModel.ProfileViewModel
import com.giron.android.view.parts.ActionBarMenuSetting

/**
 * UserProfileFragment
 */
class UserProfileFragment: Fragment(), OnMenuSettingListener, UserProfileListener {
    private val args : UserProfileFragmentArgs by navArgs()
    private lateinit var viewAdapter: ProfileAdapter
    private lateinit var binding: FragmentProfileBinding

    /**
     * ActionBarMenuSetting
     * メニュー設定
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
        binding = FragmentProfileBinding.inflate(inflater, container, false)
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

        val model = ViewModelProvider(this).get(ProfileViewModel::class.java)
        model.set(viewLifecycleOwner, args.uuid)
        viewAdapter = ProfileAdapter(model, this)

        binding.swipe.isRefreshing = true

        val manager = LinearLayoutManager(this.context)

        binding.list.apply {
            layoutManager = manager
            adapter = viewAdapter
        }

        viewAdapter.model.items.observe(viewLifecycleOwner, {
            Log.d(TAG, "update list")
            viewAdapter.notifyDataSetChanged()
            binding.swipe.isRefreshing = false
        })

        setOption()
    }

    /**
     * touchTag
     *
     * @param tag
     */
    override fun touchTag(tag: String) {
        val action = GironsNavigationDirections.actionGlobalGironTopFragment()
        action.word = tag

        navigate(action)
    }

    /**
     * touchGiron
     *
     * @param id
     * @param num
     * @param byNum
     */
    override fun touchGiron(id: Int, num: Int?, byNum: Int?) {
        val action = UserProfileFragmentDirections.actionUserProfileFragmentToGironDetail()
        action.id = id
        num?.let { action.num = it }

        navigate(action)
    }

    /**
     * static
     */
    companion object {
        const val TAG = "UserProfileFragment"
    }
}