package com.giron.android.view.giron.create

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.postDelayed
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.giron.android.databinding.FragmentCreateGironBinding
import com.giron.android.extension.closeKeyboard
import com.giron.android.extension.navigate
import com.giron.android.extension.setOption
import com.giron.android.extension.showError
import com.giron.android.model.entity.CreateGironEntity
import com.giron.android.model.entity.TagEditEntity
import com.giron.android.model.net.GironApiClient
import com.giron.android.model.net.realm.CreateGironObjApi
import com.giron.android.model.net.realm.TagObjApi
import com.giron.android.view.giron.create.viewModel.CreateGironViewModel
import com.giron.android.view.giron.detail.adapter.TagsAdapter
import com.giron.android.view.giron.detail.listener.TagEditListener
import com.giron.android.view.giron.detail.parts.TagSpannableFactory
import com.giron.android.view.parts.listener.*
import com.giron.android.view.parts.ActionBarMenuSetting
import com.giron.android.view.parts.SharedViewModelStoreOwner
import com.giron.android.view.parts.listener.OnAddPressedListener
import com.giron.android.view.parts.listener.OnBackPressedListener
import com.giron.android.view.parts.listener.OnKeyboardListener
import com.giron.android.view.parts.listener.OnMenuSettingListener
import com.giron.android.view.giron.detail.viewModel.TagViewModel
import com.google.android.flexbox.*

/**
 * CreateGironFragment
 */
class CreateGironFragment : Fragment(), SharedViewModelStoreOwner, OnMenuSettingListener,
    TagEditListener, OnKeyboardListener, OnBackPressedListener,
    OnAddPressedListener {
    private lateinit var binding: FragmentCreateGironBinding
    private lateinit var viewAdapter: TagsAdapter
    private var isCloseableKeyboard = false

    /**
     * ActionBarMenuSetting
     * メニュー設定
     */
    override val actionBarMenuSetting: ActionBarMenuSetting
        get() = ActionBarMenuSetting(navVisible = false, addVisible = true)

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
        binding = FragmentCreateGironBinding.inflate(inflater, container, false)
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

        val model = ViewModelProvider(this).get(CreateGironViewModel::class.java)
        model.set(this)
        binding.createGiron = model

        // タグリンク
        binding.officialTag.movementMethod = LinkMovementMethod.getInstance()
        binding.officialTag.setSpannableFactory(TagSpannableFactory(this))
        binding.recommendTag.movementMethod = LinkMovementMethod.getInstance()
        binding.recommendTag.setSpannableFactory(TagSpannableFactory(this))
        binding.hosTag.movementMethod = LinkMovementMethod.getInstance()
        binding.hosTag.setSpannableFactory(TagSpannableFactory(this))
        binding.recentlyTag.movementMethod = LinkMovementMethod.getInstance()
        binding.recentlyTag.setSpannableFactory(TagSpannableFactory(this))

        viewAdapter = TagsAdapter(model)

        setOption()

        // flexboxlayout
        val flexBoxLayoutManager = FlexboxLayoutManager(context)
        flexBoxLayoutManager.flexDirection = FlexDirection.ROW
        flexBoxLayoutManager.flexWrap = FlexWrap.WRAP
        flexBoxLayoutManager.justifyContent = JustifyContent.FLEX_START
        flexBoxLayoutManager.alignItems = AlignItems.CENTER

        binding.list.apply {
            adapter = viewAdapter
            layoutManager = flexBoxLayoutManager
        }

        // Enterキー入力の判定
        binding.input.setOnEditorActionListener { textView, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENDCALL) {
                // キーボード閉じる処理
                closeKeyboard()

                Log.d(TAG, "input tag = ${textView.text}")
                inputTag(textView.text.toString())

                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }

        viewAdapter.model.items.observe(viewLifecycleOwner, {
            viewAdapter.notifyDataSetChanged()
        })
    }

    /**
     * update
     */
    override fun add() {
        Log.d(TAG, "save")
        closeKeyboard()

        viewAdapter.model.items.let { list ->
            val tags = ArrayList<TagEditEntity>()
            list.forEachIndexed { i, tag ->
                if (tag is TagViewModel) {
                    tags.add(TagEditEntity.create(tag, i))
                }
            }

            val model = viewAdapter.model as CreateGironViewModel

            val entity =
                CreateGironEntity(model.title.value ?: "", model.description.value ?: "", tags)
            viewAdapter.model.isEdited = false

            GironApiClient().create(entity, {
                viewAdapter.model.addsTag.forEach { name ->
                    TagObjApi().setRecentlyData(name)
                }
                CreateGironObjApi().removeData()

                val action = CreateGironFragmentDirections.actionCreateGironFragmentToGironDetail()
                action.id = it.id
                navigate(action)
            }, { error ->
                context?.showError(error)
            })
        }
    }

    /**
     * onHideKeyboard
     */
    override fun onHideKeyboard() {
        isCloseableKeyboard = false
    }

    /**
     * onShowKeyboard
     */
    override fun onShowKeyboard() {
        binding.back.postDelayed(300) {
            // 300ms内の連続クリックを無効に
            isCloseableKeyboard = true
        }
    }

    /**
     * onBackPressed
     *
     * @return Boolean
     */
    override fun onBackPressed(): Boolean {
        closeKeyboard()

        val model = viewAdapter.model as CreateGironViewModel
        CreateGironObjApi().setData(model)

        return true
    }

    /**
     * touchTag
     *
     * @param name
     */
    override fun touchTag(name: String) {
        viewAdapter.model.add(name, context)
    }

    /**
     * listenerContext
     *
     * @return Context
     */
    override fun listenerContext(): Context {
        return requireContext()
    }

    /**
     * inputTag
     * タグの追加
     * EditTextからタグの入力
     *
     * @param name
     */
    private fun inputTag(name: String) {
        if (viewAdapter.model.add(name, context)) {
            // 追加に成功したので空にする
            viewAdapter.model.inputText.value = ""
        }
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
        const val TAG = "CreateGironFragment"
    }
}