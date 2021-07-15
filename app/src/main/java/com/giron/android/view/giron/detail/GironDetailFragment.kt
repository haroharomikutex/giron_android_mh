package com.giron.android.view.giron.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.giron.android.GironsNavigationDirections
import com.giron.android.view.main.MainActivity
import com.giron.android.databinding.FragmentGironDetailBinding
import com.giron.android.extension.*
import com.giron.android.model.entity.EmotionsEntity
import com.giron.android.model.entity.ErrorResponse
import com.giron.android.model.entity.TagsEntity
import com.giron.android.model.net.GironApiClient
import com.giron.android.view.giron.detail.listener.GironDetailListener
import com.giron.android.view.giron.detail.adapter.GironDetailAdapter
import com.giron.android.view.giron.detail.listener.EmotionButtonsListener
import com.giron.android.view.giron.detail.viewModel.GironDetailViewModel
import com.giron.android.view.giron.list.GironTopFragmentDirections
import com.giron.android.view.parts.ActionBarMenuSetting
import com.giron.android.view.parts.ShareViewModel
import com.giron.android.view.parts.SharedViewModelStoreOwner
import com.giron.android.view.parts.listener.OnBackPressedListener
import com.giron.android.view.parts.listener.OnFragmentActiveListener
import com.giron.android.view.parts.listener.OnMenuSettingListener

/**
 * GironDetailFragment
 * Giron詳細画面
 */
class GironDetailFragment : Fragment(), GironDetailListener, EmotionButtonsListener,
    OnFragmentActiveListener,
    SharedViewModelStoreOwner, OnMenuSettingListener, OnBackPressedListener {
    private val shareViewModel: ShareViewModel by lazy { ShareViewModel.get(this) }

    private val args : GironDetailFragmentArgs by navArgs()

    private lateinit var viewAdapter: GironDetailAdapter
    private lateinit var binding: FragmentGironDetailBinding

    /**
     * ActionBarMenuSetting
     * メニュー設定
     */
    override val actionBarMenuSetting: ActionBarMenuSetting
        get() = ActionBarMenuSetting(navVisible = false)

    /**
     * onCreateView
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return View
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGironDetailBinding.inflate(inflater, container, false)
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

        val key = "${findNavController().backStack.count()}_${mainActivity.topFragment?.javaClass?.name}"
        val model = ViewModelProvider(this).get(key, GironDetailViewModel::class.java)

        // Inflate the layout for this fragment
        viewAdapter = GironDetailAdapter(viewLifecycleOwner, model, this)

        // set model
        binding.swipe.isRefreshing = model.set(args.id, args.num, viewLifecycleOwner, this)
        binding.giron = model

        setOption()

        // メインのリスト
        binding.listView.apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        binding.swipe.setOnRefreshListener {
            model.reset()
        }
    }

    /**
     * active
     */
    override fun active() {
        val act = activity
        if (act is AppCompatActivity) {
            act.supportActionBar?.title = viewAdapter.model.title
        }
    }

    /**
     * write
     *
     * @param message 書き込み文字列
     */
    override fun write(message: String) {
        // 空であればその場で終了
        if (message.isEmpty()) return

        GironApiClient().createComment(args.id, message, {
            viewAdapter.model.comment.value = ""

            closeKeyboard()

            // 新しくコメントを取得する
            viewAdapter.model.newComment()
        }, {
            context?.showError(it)
        })
    }

    /**
     * reload
     */
    override fun reload() {
        viewAdapter.notifyDataSetChanged()
        binding.swipe.isRefreshing = false
        if (viewAdapter.model.scrollNum > 0) scroll(viewAdapter.model.scrollNum)
    }

    /**
     * scroll
     *
     * @param num Scroll to Comment Number
     */
    private fun scroll(num: Int) {
        Log.d(TAG, "scroll to $num")
        viewAdapter.model.getPosByNum(num)?.let {
            binding.listView.scrollToPosition(it)
        }
    }

    /**
     * insert
     *
     * @param pos 挿入位置
     * @param count 挿入数
     */
    override fun insert(pos: Int, count: Int) {
        viewAdapter.notifyItemRangeInserted(pos, count)
        binding.swipe.isRefreshing = false
        if (viewAdapter.model.scrollNum > 0) scroll(viewAdapter.model.scrollNum)
        Log.d(TAG, "insert ${viewAdapter.model.scrollNum}")
    }

    /**
     * setTitle
     * タイトルをセットする
     *
     * @param title タイトル
     */
    override fun setTitle(title: String) {
        val act = activity as AppCompatActivity
        act.supportActionBar?.title = title
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
     * openReword
     * リワードモーダルを開く
     *
     * @param id GironID
     * @param num Comment Number
     * @param callback shareViewModelに格納する処理成功時のcallback
     */
    override fun openReword(id: Int, num: Int?, callback: (coin: Int) -> Unit) {
        // set callback
        shareViewModel.rewordByFragment = callback

        val action = RewordsDialogFragmentDirections.actionGlobalRewordsDialogFragment()
        action.id = id
        num?.let { action.num = it }

        navigate(action)
    }

    /**
     * openAction
     * アクションモーダルを開く
     *
     * @param id GironID
     * @param rewordCallback shareViewModelに格納するコイン処理成功時のcallback
     * @param emotionCallback shareViewModelに格納するエモーション処理成功時のcallback
     * @param copyCallback
     * @param replyCallback
     */
    override fun openAction(id: Int, rewordCallback: (coin: Int) -> Unit, emotionCallback: (entity: EmotionsEntity) -> Unit, copyCallback: () -> Unit, replyCallback: () -> Unit) {
        shareViewModel.rewordByFragment = rewordCallback
        shareViewModel.addEmotionByFragment = emotionCallback
        shareViewModel.copyByFragment = copyCallback
        shareViewModel.replyByFragment = replyCallback

        val action = ActionDialogFragmentDirections.actionGlobalActionDialogFragment()
        action.id = id

        navigate(action)
    }

    /**
     * openEmotionListGiron
     * エモーションリストを開く
     *
     * @param id GironID
     * @param callback shareViewModelに格納する処理成功時のcallback
     */
    override fun openEmotionListGiron(id: Int, view: View, callback: (entity: EmotionsEntity) -> Unit) {
        // set callback
        shareViewModel.addEmotionByFragment = callback

        val array = IntArray(2)
        view.getLocationInWindow(array)

        // ボタンの位置を取得
        Log.d(TAG, "location = x:${array[0]} y:${array[1]}")
        Log.d(TAG, "height:${view.height}")

        val activity = activity as MainActivity
        val contentTop = activity.window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT).top

        val action = EmotionsDialogFragmentDirections.actionGlobalEmotionsDialogFragment()
        action.modelId = id
        action.y = array[1] + view.height - contentTop
        action.type = "giron"

        navigate(action)
    }

    /**
     *openEmotionListComment
     * エモーションリストを開く
     *
     * @param id GironID
     * @param num Comment Number
     * @param callback shareViewModelに格納する処理成功時のcallback
     */
    override fun openEmotionListComment(id: Int, num: Int, view: View, callback: (entity: EmotionsEntity) -> Unit) {
        // set callback
        shareViewModel.addEmotionByFragment = callback

        val array = IntArray(2)
        view.getLocationInWindow(array)

        // ボタンの位置を取得
        Log.d(TAG, "location = x:${array[0]} y:${array[1]}")
        Log.d(TAG, "height:${view.height}")

        val activity = activity as MainActivity
        val contentTop = activity.window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT).top

        val action = EmotionsDialogFragmentDirections.actionGlobalEmotionsDialogFragment()
        action.modelId = id
        action.y = array[1] + view.height - contentTop
        action.type = "comment"

        navigate(action)
    }

    /**
     * openTagEdit
     * タグ編集画面を開く
     *
     * @param id GironID
     * @param callback shareViewModelに格納する処理成功時のcallback
     */
    override fun openTagEdit(id: Int, callback: (entity: TagsEntity) -> Unit) {
        // set callback
        shareViewModel.tagEditByFragment = callback

        val action = GironDetailFragmentDirections.actionGironDetailFragmentToTagsFragment(id)
        action.isOwner = viewAdapter.model.isFirst

        navigate(action)
    }

    /**
     * openCommentWrite
     * コメント書き込み画面を開く
     *
     * @param id GironID
     */
    override fun openCommentWrite(id: Int) {
        // コメント書き込み後の処理
        shareViewModel.writeCommentByFragment = {
            // 最後のページの場合は、新しくコメントを取得する
            // そうでない場合は、numを元にコメントを取得する
            viewAdapter.model.scrollNum = it
            if (viewAdapter.model.isLastPage())
                viewAdapter.model.newComment()
            else
                viewAdapter.model.getCommentByNum(it)

            Log.d(TAG, "set scroll num $it")
        }

        val action =
            GironDetailFragmentDirections.actionGironDetailFragmentToCreateCommentFragment()
        action.id = id
        action.title = viewAdapter.model.title

        navigate(action)
    }

    /**
     * touchGiron
     *
     * @param id GironID
     * @param num Comment Number
     * @param byNum Clicked Comment Number
     */
    override fun touchGiron(id: Int, num: Int?, byNum: Int?) {
        val action = GironDetailFragmentDirections.actionGlobalGironDetailFragment()
        action.id = id
        num?.let { action.num = it }

        navigate(action)
    }

    /**
     * touchComment
     * コメントタッチ
     *
     * @param num Comment Number
     * @param byNum Clicked Comment Number
     */
    override fun touchComment(num: Int, byNum: Int?) {
        // 表示されている場合はそのままスクロール
        if (viewAdapter.model.hasNum(num)) {
            scroll(num)
        }
        // ない場合は遷移
        else {
            val action = GironDetailFragmentDirections.actionGlobalGironDetailFragment()
            action.id = id
            action.num = num

            navigate(action)
        }
    }

    /**
     * touchTag
     *
     * @param tag
     */
    override fun touchTag(tag: String) {
        val action = GironTopFragmentDirections.actionGlobalGironTopFragment()
        action.word = tag

        findNavController().navigate(action)
    }

    /**
     * showError
     *
     * @param error
     */
    override fun showError(error: ErrorResponse) {
        context?.showError(error)
    }

    /**
     * touchUser
     *
     * @param uuid
     */
    override fun touchUser(uuid: String) {
        val action = GironsNavigationDirections.actionGlobalUserProfileFragment(uuid)
        findNavController().navigate(action)
    }

    /**
     * link
     *
     * @param url
     */
    override fun link(url: String) {
        Log.d(TAG, "link $url")

        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    /**
     * onBackPressed
     *
     * @return Boolean
     */
    override fun onBackPressed(): Boolean {
        findNavController().navigateUp()
        return false
    }

    /**
     * copyClipBoard
     *
     * @param string
     */
    override fun copyClipBoard(string: String) {
        context?.setClipboard(string)
        context?.showAlertDialog("copied_clipboard".locate())
    }

    /**
     * static
     */
    companion object {
        const val TAG = "GironDetailFragment"
    }
}
