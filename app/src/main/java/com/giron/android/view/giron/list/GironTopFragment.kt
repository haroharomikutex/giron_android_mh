package com.giron.android.view.giron.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.giron.android.R
import com.giron.android.databinding.FragmentGironTopBinding
import com.giron.android.extension.mainActivity
import com.giron.android.extension.setOption
import com.giron.android.view.giron.list.viewModel.GironTopFactory
import com.giron.android.view.giron.list.viewModel.GironTopViewModel
import com.giron.android.view.parts.listener.OnKeyboardListener
import com.giron.android.view.parts.listener.OnMenuSettingListener
import com.giron.android.view.parts.ActionBarMenuSetting
import com.giron.android.view.parts.ShareViewModel
import com.giron.android.view.parts.SharedViewModelStoreOwner

/**
 * HomeFragment
 * トップ画面
 */
class GironTopFragment : Fragment(), OnMenuSettingListener, OnKeyboardListener,
    SharedViewModelStoreOwner {
    private val shareViewModel: ShareViewModel by lazy { ShareViewModel.get(this) }
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var binding: FragmentGironTopBinding

    private val args: GironTopFragmentArgs by navArgs()

    /**
     * actionBarMenuSetting
     */
    override val actionBarMenuSetting: ActionBarMenuSetting
        get() = ActionBarMenuSetting(searchVisible = true)

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
        shareViewModel.searchByFragment.clear()
        binding = FragmentGironTopBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
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

        val model = ViewModelProvider(this, GironTopFactory(args.word, mainActivity.topFragmentListener?.isCreatableGiron ?: false)).get(findNavController().backStack.count().toString(), GironTopViewModel::class.java)
        binding.top = model

        Log.d(TAG, "onViewCreated ${model.word.value}")

        val titles: ArrayList<String> = arrayListOf(getString(R.string.update_giron_list_btn), getString(R.string.new_giron_list_btn) , getString(R.string.mygiron_giron_list_btn))
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager, titles, model)
        mainActivity.setKeyBoardValue(model.word.value ?: "")

        setOption()

        binding.gironViewpager.adapter = viewPagerAdapter

        binding.create.setOnClickListener {create()}
    }

    /**
     * create
     */
    fun create() {
        Log.d(TAG, "create")

        val action = GironTopFragmentDirections.actionGironTopFragmentToCreateGironFragment()
        findNavController().navigate(action)
    }

    /**
     * onResume
     */
    override fun onResume() {
        Log.d(TAG, "onResume")

        mainActivity.setFragment(this)

        super.onResume()
    }

    override fun onShowKeyboard() {
        Log.d(TAG, "onShowKeyboard")


        shareViewModel.setSearchWordByFragment = { word ->
            binding.top?.word?.value = word
            mainActivity.setKeyBoardValue(word)
        }

        val action = GironTopFragmentDirections.actionGironTopFragmentToSearchCandidateFragment()
        findNavController().navigate(action)
    }

    override fun onHideKeyboard() {
        Log.d(TAG, "onHideKeyboard")
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
     * ViewPagerAdapter
     *
     * @param fm
     * @param titles
     */
    class ViewPagerAdapter(fm: FragmentManager, titles: ArrayList<String>, val model: GironTopViewModel) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val titleList = titles

        /**
         * getCount
         *
         * @return Int
         */
        override fun getCount(): Int {
            return titleList.count()
        }

        /**
         * getItem
         *
         * @param Int position
         *
         * @return Fragment
         */
        override fun getItem(position: Int): Fragment {
            Log.d(TAG, "getItem $position ${model.word.value}")
            return when (position) {
                0 -> {
                    GironListFragment.newInstance("update", model.word.value!!)
                }
                1 -> {
                    GironListFragment.newInstance("new", model.word.value!!)
                }
                else -> {
                    GironListFragment.newInstance("mygiron", model.word.value!!)
                }
            }
        }

        /**
         * getPageTitle
         *
         * @param position
         *
         * @return CharSequence
         */
        override fun getPageTitle(position: Int): CharSequence {
            return titleList[position]
        }

    }

    /**
     * static
     */
    companion object {
        const val TAG = "GironTopFragment"
    }
}