package com.giron.android.view.giron.detail

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelStore
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.giron.android.R
import com.giron.android.databinding.FragmentEditTagBinding
import com.giron.android.extension.closeKeyboard
import com.giron.android.extension.setOption
import com.giron.android.extension.showError
import com.giron.android.model.entity.TagEditEntity
import com.giron.android.model.entity.TagsEditEntity
import com.giron.android.model.net.realm.TagObjApi
import com.giron.android.model.net.GironApiClient
import com.giron.android.view.giron.detail.adapter.TagsAdapter
import com.giron.android.view.giron.detail.listener.TagEditListener
import com.giron.android.view.giron.detail.parts.TagSpannableFactory
import com.giron.android.view.giron.detail.viewModel.TagViewModel
import com.giron.android.view.giron.detail.viewModel.TagsViewModel
import com.giron.android.view.parts.ActionBarMenuSetting
import com.giron.android.view.parts.ShareViewModel
import com.giron.android.view.parts.SharedViewModelStoreOwner
import com.giron.android.view.parts.listener.OnBackPressedListener
import com.giron.android.view.parts.listener.OnMenuSettingListener
import com.giron.android.view.parts.listener.OnUpdatePressedListener
import com.google.android.flexbox.*

/**
 * TagsFragment
 *
 * タグ編集画面
 */
class TagsFragment : Fragment(), SharedViewModelStoreOwner, OnBackPressedListener,
    OnUpdatePressedListener, OnMenuSettingListener, TagEditListener {
    private val shareViewModel: ShareViewModel by lazy { ShareViewModel.get(this) }
    private val args: TagsFragmentArgs by navArgs()
    private val model: TagsViewModel by viewModels()
    private lateinit var viewAdapter: TagsAdapter
    private lateinit var binding: FragmentEditTagBinding

    /**
     * ActionBarMenuSetting
     * メニュー設定
     */
    override val actionBarMenuSetting: ActionBarMenuSetting
        get() = ActionBarMenuSetting(navVisible = false, updateVisible = true)

     /**
     * onCreateView
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView")
        viewAdapter = TagsAdapter(model)

        binding = FragmentEditTagBinding.inflate(inflater, container, false)

        // タグリンク
        binding.officialTag.movementMethod = LinkMovementMethod.getInstance()
        binding.officialTag.setSpannableFactory(TagSpannableFactory(this))
        binding.recommendTag.movementMethod = LinkMovementMethod.getInstance()
        binding.recommendTag.setSpannableFactory(TagSpannableFactory(this))
        binding.hosTag.movementMethod = LinkMovementMethod.getInstance()
        binding.hosTag.setSpannableFactory(TagSpannableFactory(this))
        binding.recentlyTag.movementMethod = LinkMovementMethod.getInstance()
        binding.recentlyTag.setSpannableFactory(TagSpannableFactory(this))

        return binding.root
    }

    /**
     * onViewCreated
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOption()

        model.set(args.id, viewLifecycleOwner, args.isOwner)
        model.getData()

        binding.tags = model
        binding.lifecycleOwner = viewLifecycleOwner

        val act = activity as AppCompatActivity
        act.supportActionBar?.title = context?.getString(R.string.tag_edit)

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
     * touchTag
     * おすすめなどのタグをタップした際のイベント
     * タップされたタグを追加する
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
     * save
     * 保存する
     */
    override fun update() {
        Log.d(TAG, "save")

        // 変更されていない場合はそのまま終了
        if (!viewAdapter.model.isEdited) {
            findNavController().navigateUp()
            return
        }

        viewAdapter.model.items.let { list ->
            val tags = ArrayList<TagEditEntity>()
            list.forEachIndexed { i, tag ->
                if (tag is TagViewModel) {
                    tags.add(TagEditEntity.create(tag, i))
                }
            }

            val entity = TagsEditEntity(tags)
            viewAdapter.model.isEdited = false

            GironApiClient().editTag(args.id, entity, {
                shareViewModel.tagEditByFragment?.invoke(it)

                viewAdapter.model.addsTag.forEach{name ->
                    TagObjApi().setRecentlyData(name)
                }

                findNavController().navigateUp()
            }, { error ->
                context?.showError(error)
            })
        }
    }

    /**
     * onBackPressed
     * 戻るボタンが押下された時
     */
    override fun onBackPressed(): Boolean {
        // Retract the keyboard
        closeKeyboard()

        if (viewAdapter.model.isEdited) {
            context?.let {
                AlertDialog
                        .Builder(it)
                        .setMessage(it.getString(R.string.alert_editing))
                        .setPositiveButton("OK") { _, _ ->
                            viewAdapter.model.isEdited = false
                            findNavController().navigateUp()
                        }.setNegativeButton("Cancel", null)
                        .show()
                return false
            }
        }
        return true
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
        private const val TAG = "TagsFragment"
    }
}