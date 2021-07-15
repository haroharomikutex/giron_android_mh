package com.giron.android.view.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.giron.android.R
import com.giron.android.databinding.FragmentMypageTopBinding
import com.giron.android.view.parts.listener.TopFragmentListener

/**
 * MyPageTopFragment
 */
class MyPageTopFragment : Fragment(), TopFragmentListener {
    private lateinit var binding: FragmentMypageTopBinding

    /**
     * current Fragment
     */
    override val currentFragment: Fragment
        get() {
            val fragment = childFragmentManager
                .findFragmentById(R.id.mypage_host)
                ?.childFragmentManager
                ?.fragments
                ?.get(0)

            fragment?.let { return it }

            return this
        }

    /**
     * isPlayHomeAsUpEnabled
     * 戻るボタンを表示するか
     */
    override val isPlayHomeAsUpEnabled: Boolean
        get() {
            val count = childFragmentManager
                .findFragmentById(R.id.mypage_host)
                ?.childFragmentManager
                ?.backStackEntryCount ?: 0

            Log.d(TAG, "isPlayHomeAsUpEnabled $count")

            return count > 0
        }

    /**
     * backStackCount
     * バックスタック数
     */
    override val backStackCount: Int
        get() {
            return childFragmentManager
                .findFragmentById(R.id.mypage_host)
                ?.childFragmentManager
                ?.backStackEntryCount ?: 0
        }

    /**
     * isCreatableGiron
     * Gironを作成できるか
     */
    override val isCreatableGiron: Boolean
        get() = false

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
        binding = FragmentMypageTopBinding.inflate(inflater, container, false)
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

        // startDestinationの変更
        childFragmentManager.findFragmentById(R.id.mypage_host)?.let { fragment ->
            val navController = fragment.findNavController()
            val graph = navController.navInflater.inflate(R.navigation.girons_navigation)
            fragment.findNavController().graph = graph.apply {
                startDestination = R.id.myPageFragment
            }
        }

    }

    /**
     * back
     *
     * @return Boolean Was the process successful?
     */
    override fun back(): Boolean {
        if (!isPlayHomeAsUpEnabled)
            return false

        // popBackStack
        binding.mypageHost.findNavController().navigateUp()
        return true
    }

    /**
     * static
     */
    companion object {
        const val TAG = "MyPageTopFragment"
    }
}