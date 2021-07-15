package com.giron.android.view.giron

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.giron.android.R
import com.giron.android.databinding.FragmentHomeBinding
import com.giron.android.view.parts.listener.TopFragmentListener

/**
 * HomeFragment
 * トップ画面
 */
class TopFragment : Fragment(), TopFragmentListener {
    private lateinit var binding: FragmentHomeBinding

    /**
     * current Fragment
     */
    override val currentFragment: Fragment
        get(){
            val fragment = childFragmentManager
                    .findFragmentById(R.id.home_host)
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
                    .findFragmentById(R.id.home_host)
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
                    .findFragmentById(R.id.home_host)
                    ?.childFragmentManager
                    ?.backStackEntryCount ?: 0
        }

    /**
     * isCreatableGiron
     * Gironを作成できるか
     */
    override val isCreatableGiron: Boolean
        get() = true

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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
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
        binding.homeHost.findNavController().navigateUp()
        return true
    }

    /**
     * static
     */
    companion object {
        const val TAG = "HomeFragment"
    }
}