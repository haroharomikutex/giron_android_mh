@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.navigation.fragment.findNavController
import com.giron.android.GironsNavigationDirections
import com.giron.android.R
import com.giron.android.databinding.FragmentMypageBinding
import com.giron.android.extension.setOption
import com.giron.android.model.net.realm.UserObjApi
import com.giron.android.util.CircleOutlineProvider
import com.giron.android.view.mypage.viewModel.MyPageFactory
import com.giron.android.view.mypage.viewModel.MyPageViewModel
import com.giron.android.view.parts.listener.OnFragmentActiveListener
import com.giron.android.view.parts.listener.OnMenuSettingListener
import com.giron.android.view.parts.ActionBarMenuSetting
import com.giron.android.view.parts.ShareViewModel
import com.giron.android.view.parts.SharedViewModelStoreOwner
import com.giron.android.view.mypage.MyPageFragmentDirections

/**
 * MyPageFragment
 */
class MyPageFragment : Fragment(),
    SharedViewModelStoreOwner,
    OnMenuSettingListener,
    OnFragmentActiveListener
{
    private val shareViewModel: ShareViewModel by lazy { ShareViewModel.get(this) }
    private lateinit var binding: FragmentMypageBinding

    /**
     * actionBarMenuSetting
     */
    override val actionBarMenuSetting: ActionBarMenuSetting
        get() = ActionBarMenuSetting()

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    /**
     * onViewCreated
     *
     * @param View
     * @param Bundle?
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProvider(this, MyPageFactory(viewLifecycleOwner)).get(MyPageViewModel::class.java)
        model.reload()

        binding.mypage = model
        binding.userImage.outlineProvider = CircleOutlineProvider()

        setOption()

        binding.profile.setOnClickListener {
            val action =
            GironsNavigationDirections.actionGlobalUserProfileFragment(UserObjApi().getData()?.uuid ?: "")

            findNavController().navigate(action)
        }

        binding.edit.setOnClickListener {
            shareViewModel.editProfileByFragment = {
                model.set(it)
            }

            val action = MyPageFragmentDirections.actionMyPageFragmentToEditProfileFragment()
            findNavController().navigate(action)
        }

        binding.GCoin.setOnClickListener {
            val action = MyPageFragmentDirections.actionGlobalCoinFragment()
            findNavController().navigate(action)
        }

        binding.news.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageFragmentToInformationFragment()
            findNavController().navigate(action)
        }

        binding.setting.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageFragmentToSettingFragment()
            findNavController().navigate(action)
        }
    }

    /**
     * active
     */
    override fun active() {
        val act = activity as AppCompatActivity
        act.supportActionBar?.title = context?.getString(R.string.title_mypage)
        binding.mypage?.reload()
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
}