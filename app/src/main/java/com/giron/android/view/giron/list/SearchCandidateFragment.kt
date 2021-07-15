package com.giron.android.view.giron.list

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
import com.giron.android.databinding.FragmentSearchCandidateListBinding
import com.giron.android.extension.closeKeyboard
import com.giron.android.extension.mainActivity
import com.giron.android.extension.setOption
import com.giron.android.view.giron.list.adapter.SearchCandidateAdapter
import com.giron.android.view.giron.list.listener.SearchCandidateListener
import com.giron.android.view.giron.list.viewModel.SearchCandidatesViewModel
import com.giron.android.view.parts.listener.OnBackPressedListener
import com.giron.android.view.parts.listener.OnKeyboardListener
import com.giron.android.view.parts.listener.OnMenuSettingListener
import com.giron.android.view.parts.listener.OnSearchViewListener
import com.giron.android.view.parts.ActionBarMenuSetting
import com.giron.android.view.parts.ShareViewModel
import com.giron.android.view.parts.SharedViewModelStoreOwner

/**
 * SearchCandidateFragment
 * 検索候補画面
 */
class SearchCandidateFragment: Fragment(), OnMenuSettingListener, OnSearchViewListener,
    OnKeyboardListener, OnBackPressedListener, SearchCandidateListener,
    SharedViewModelStoreOwner {
    private val shareViewModel: ShareViewModel by lazy { ShareViewModel.get(this) }
    private lateinit var viewAdapter: SearchCandidateAdapter
    private lateinit var binding: FragmentSearchCandidateListBinding

    /**
     * actionBarMenuSetting
     */
    override val actionBarMenuSetting: ActionBarMenuSetting
        get() = ActionBarMenuSetting(searchVisible = true, navVisible = false)

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
        binding = FragmentSearchCandidateListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

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

        val model = ViewModelProvider(this).get(SearchCandidatesViewModel::class.java)
        model.set(viewLifecycleOwner, requireContext(), this)
        binding.search = model

        viewAdapter = SearchCandidateAdapter(viewLifecycleOwner, model)

        setOption()

        binding.listView.apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        viewAdapter.model.items.observe(viewLifecycleOwner, {
            Log.d(TAG, "notifyDataSetChanged")
            viewAdapter.notifyDataSetChanged()
        })
    }

    /**
     * search
     *
     * @param word
     */
    override fun search(word: String) {
        Log.d(TAG, "search $word")

        mainActivity.setKeyBoardValue(word)
        finish(word)
    }

    /**
     * finish
     *
     * @param word
     */
    private fun finish(word: String) {
        shareViewModel.searchByFragment.forEach {
            it.invoke(word)
        }
        shareViewModel.setSearchWordByFragment?.invoke(word)

        closeKeyboard()
        view?.requestFocus()
        findNavController().popBackStack()
    }

    override fun focusChange(view: View, b: Boolean) {
        Log.d(TAG, "searchView focusChange")
    }

    override fun onShowKeyboard() {
        Log.d(TAG, "onShowKeyboard")
    }

    override fun onHideKeyboard() {
        Log.d(TAG, "onHideKeyboard")
    }

    override fun onQueryTextChange(text: String?): Boolean {
        Log.d(TAG, "onQueryTextChange $text")
        val word = text ?: ""
        viewAdapter.model.setWord(word)
        return true
    }

    override fun onQueryTextSubmit(text: String?): Boolean {
        Log.d(TAG, "onQueryTextSubmit $text")

        closeKeyboard()
        view?.requestFocus()

        text?.let { finish(text) }

        return true
    }

    override fun clickCloseButton() {
        Log.d(TAG, "clickCloseButton")
    }

    /**
     * onBackPressed
     *
     * @return Boolean
     */
    override fun onBackPressed(): Boolean {
        closeKeyboard()
        view?.requestFocus()
        return true
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
     * static
     */
    companion object {
        const val TAG = "SearchCandidateFragment"
    }
}