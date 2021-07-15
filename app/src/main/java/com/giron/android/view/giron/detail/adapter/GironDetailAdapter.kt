@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.giron.detail.adapter

import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.giron.android.R
import com.giron.android.databinding.ItemCommentGironDetailBinding
import com.giron.android.databinding.ItemGironDetailArrowsBinding
import com.giron.android.databinding.ItemGironDetailBinding
import com.giron.android.model.net.CommentApiClient
import com.giron.android.model.net.GironApiClient
import com.giron.android.model.net.realm.WritingCommentObjApi
import com.giron.android.util.CircleOutlineProvider
import com.giron.android.view.giron.detail.listener.EmotionButtonsListener
import com.giron.android.view.giron.detail.listener.GironDetailListener
import com.giron.android.view.giron.detail.listener.GironDetailTagListener
import com.giron.android.view.giron.detail.parts.CommentSpannableFactory
import com.giron.android.view.giron.detail.parts.GironDetailDescriptionSpannableFactory
import com.giron.android.view.giron.detail.parts.GironDetailTagSpannableFactory
import com.giron.android.view.giron.detail.viewModel.*
import com.giron.android.view.giron.detail.viewModel.*
import com.giron.android.view.parts.BaseViewAdapter
import com.giron.android.view.parts.BaseViewHolder
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.ViewModelType
import com.google.android.flexbox.*

/**
 * GironDetailAdapter
 *
 * @param LifecycleOwner
 * @param GironDetailViewModel
 * @param GironDetailListener
 */
class GironDetailAdapter(private val owner: LifecycleOwner, override val model: GironDetailViewModel, private val listener: GironDetailListener) : BaseViewAdapter(model) {
    /**
     * onCreateViewHolder
     *
     * @param ViewGroup
     * @param Int
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when(ViewModelType.fromInt(viewType)) {
            ViewModelType.Giron -> {
                val binding = DataBindingUtil.inflate<ItemGironDetailBinding>(inflater, R.layout.item_giron_detail, parent, false)
                binding.lifecycleOwner = owner
                GironsDetailViewHolder(binding, listener, owner)
            }
            ViewModelType.Arrows -> {
                val binding = DataBindingUtil.inflate<ItemGironDetailArrowsBinding>(inflater, R.layout.item_giron_detail_arrows, parent, false)
                binding.lifecycleOwner = owner
                ArrowsViewHolder(binding)
            }
            ViewModelType.Comment -> {
                val binding = ItemCommentGironDetailBinding.inflate(inflater, parent, false)
                binding.lifecycleOwner = owner
                CommentViewHolder(binding, listener, owner)
            }
            else -> {
                val binding = ItemCommentGironDetailBinding.inflate(inflater, parent, false)
                binding.lifecycleOwner = owner
                CommentViewHolder(binding, listener, owner)
            }
        }
    }

    /**
     * GironsDetailViewHolder
     *
     * @param ItemGironDetailBinding
     * @param GironDetailListener
     * @param LifecycleOwner
     */
    class GironsDetailViewHolder(val binding: ItemGironDetailBinding, val listener: GironDetailListener, owner: LifecycleOwner) : BaseViewHolder(binding.root),
        EmotionButtonsListener, GironDetailTagListener {
        private var viewAdapter: EmotionButtonsAdapter

        /**
         * init
         */
        init {
            // radius
            binding.image.outlineProvider = CircleOutlineProvider()

            binding.description.movementMethod = LinkMovementMethod.getInstance()
            binding.description.setSpannableFactory(GironDetailDescriptionSpannableFactory(this))
            binding.tag.movementMethod = LinkMovementMethod.getInstance()
            binding.tag.setSpannableFactory(GironDetailTagSpannableFactory(this))

            val model = EmotionButtonsViewModel(this)
            viewAdapter = EmotionButtonsAdapter(owner, model, this)

            // flexboxlayout
            val flexboxLayoutManager = FlexboxLayoutManager(binding.root.context)
            flexboxLayoutManager.flexDirection = FlexDirection.ROW
            flexboxLayoutManager.flexWrap = FlexWrap.WRAP
            flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
            flexboxLayoutManager.alignItems = AlignItems.CENTER

            binding.emotions.apply {
                adapter = viewAdapter
                layoutManager = flexboxLayoutManager
            }

            viewAdapter.model.items.observe(owner, Observer {
                viewAdapter.notifyDataSetChanged()
            })
        }

        /**
         * set
         *
         * @param BaseViewModel
         */
        override fun set(_model: BaseViewModel) {
            val model = _model as GironViewModel
            binding.giron = model
            viewAdapter.model.setItems(model.giron.emotions, model.giron.IsEmotions)
        }

        /**
         * addEmotion
         * EmotionButtonsListenerの実装関数
         *
         * @param EmotionType
         */
        override fun addEmotion(type: EmotionType) {
            Log.d(TAG, "addEmotion")

            binding.giron?.id?.let {
                GironApiClient().emotion(it, type, {entity ->
                    viewAdapter.model.setItems(entity.emotions, entity.IsEmotions)
                }, { err ->
                    listener.showError(err)
                })
            }
        }

        /**
         * removeEmotion
         * EmotionButtonsListenerの実装
         *
         * @param EmotionType
         */
        override fun removeEmotion(type: EmotionType) {
            Log.d(TAG, "removeEmotion")

            binding.giron?.id?.let {
                GironApiClient().cancelEmotion(it, type, {entity ->
                    viewAdapter.model.setItems(entity.emotions, entity.IsEmotions)
                }, {
                })
            }
        }

        /**
         * openEmotionList
         * EmotionButtonsListenerの実装
         *
         * @param View
         */
        override fun openEmotionList(view: View) {
            Log.d(TAG, "openEmotionList")

            binding.giron?.id?.let {
                listener.openEmotionListGiron(it, view) { entity ->
                    viewAdapter.model.setItems(entity)
                }
            }
        }

        /**
         * addTag
         * GironDetailTagListenerの実装
         */
        override fun addTag() {
            binding.giron?.addTag()
        }

        /**
         * static
         */
        companion object {
            private const val TAG = "GironsDetailViewHolder"
        }
    }

    /**
     * ArrowsViewHolder
     * 上部のpager
     *
     * @param ItemGironDetailArrowsBinding
     */
    class ArrowsViewHolder(val binding: ItemGironDetailArrowsBinding): BaseViewHolder(binding.root) {
        /**
         * set
         *
         * @param BaseViewModel
         */
        override fun set(_model: BaseViewModel) {
            val model = _model as ArrowsViewModel
            binding.arrows = model
        }
    }

    /**
     * CommentViewHolder
     * コメント
     *
     * @param ItemCommentGironDetailBinding
     * @param GironDetailListener
     * @param LifecycleOwner
     */
    class CommentViewHolder(val binding: ItemCommentGironDetailBinding, val listener: GironDetailListener, owner: LifecycleOwner) : BaseViewHolder(binding.root),
        EmotionButtonsListener {
        private var viewAdapter: EmotionButtonsAdapter
        private var selfViewAdapter: EmotionButtonsAdapter

        /**
         * init
         */
        init {
            // radius
            binding.image.outlineProvider = CircleOutlineProvider()
            binding.selfImage.outlineProvider = CircleOutlineProvider()

            binding.description.movementMethod = LinkMovementMethod.getInstance()
            binding.description.setSpannableFactory(CommentSpannableFactory(this))

            val model = EmotionButtonsViewModel(this)
            viewAdapter = EmotionButtonsAdapter(owner, model, this)

            // flexboxlayout
            val flexboxLayoutManager = FlexboxLayoutManager(binding.root.context)
            flexboxLayoutManager.flexDirection = FlexDirection.ROW
            flexboxLayoutManager.flexWrap = FlexWrap.WRAP
            flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
            flexboxLayoutManager.alignItems = AlignItems.CENTER

            binding.emotions.apply {
                adapter = viewAdapter
                layoutManager = flexboxLayoutManager
            }

            viewAdapter.model.items.observe(owner, Observer {
                Log.d(TAG, "update list")
                viewAdapter.notifyDataSetChanged()
            })

            /**
             * self
             */
            binding.selfDescription.movementMethod = LinkMovementMethod.getInstance()
            binding.selfDescription.setSpannableFactory(CommentSpannableFactory(this))

            val selfEmotionModel = EmotionButtonsViewModel(null, false)
            selfViewAdapter = EmotionButtonsAdapter(owner, selfEmotionModel)

            // flexboxlayout
            val selfFlexboxLayoutManager = FlexboxLayoutManager(binding.root.context)
            flexboxLayoutManager.flexDirection = FlexDirection.ROW
            flexboxLayoutManager.flexWrap = FlexWrap.WRAP
            flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
            flexboxLayoutManager.alignItems = AlignItems.CENTER

            binding.selfEmotions.apply {
                adapter = selfViewAdapter
                layoutManager = selfFlexboxLayoutManager
            }

            selfViewAdapter.model.items.observe(owner, Observer {
                Log.d(TAG, "update list")
                selfViewAdapter.notifyDataSetChanged()
            })
        }

        /**
         * addEmotion
         * EmotionButtonsListenerの実装関数
         *
         * @param EmotionType
         */
        override fun addEmotion(type: EmotionType) {
            Log.d(TAG, "addEmotion")

            binding.comment?.id?.let { it ->
                CommentApiClient().emotion(it, type, { entity ->
                    viewAdapter.model.setItems(entity.emotions, entity.IsEmotions)
                }, { err ->
                    listener.showError(err)
                })
            }
        }

        /**
         * addEmotion
         * EmotionButtonsListenerの実装関数
         *
         * @param EmotionType
         */
        override fun removeEmotion(type: EmotionType) {
            Log.d(TAG, "removeEmotion")

            binding.comment?.id?.let {
                CommentApiClient().cancelEmotion(it, type, { entity ->
                    viewAdapter.model.setItems(entity.emotions, entity.IsEmotions)
                }, {})
            }
        }

        /**
         * openEmotionList
         * EmotionButtonsListenerの実装
         *
         * @param View
         */
        override fun openEmotionList(view: View) {
            Log.d(TAG, "openEmotionList")

            binding.comment?.id?.let {id ->
                binding.comment?.comment?.num?.let {num ->
                    listener.openEmotionListComment(id, num, view) { entity ->
                        viewAdapter.model.setItems(entity)
                    }
                }
            }
        }

        /**
         * set
         *
         * @param BaseViewModel
         */
        override fun set(_model: BaseViewModel) {
            val model = _model as CommentViewModel
            binding.comment = model
            viewAdapter.model.setItems(model.comment.emotions, model.comment.IsEmotions)
            selfViewAdapter.model.setItems(model.comment.emotions, model.comment.IsEmotions)

            binding.title.setOnLongClickListener { openAction(model) }
            binding.description.setOnLongClickListener { openAction(model) }
        }

        /**
         * openAction
         * アクションモーダル画面を開く
         *
         * @param CommentViewModel
         */
        private fun openAction(model: CommentViewModel) : Boolean {
            Log.d(TAG, "openAction")

            listener.openAction(model.id, rewordCallback = { coin ->
                model.coinCount.value = coin.toString()
            }, emotionCallback = { entity ->
                viewAdapter.model.setItems(entity)
            }, replyCallback = {
                model.giron?.let {giron ->
                    var comment = WritingCommentObjApi().getData(giron.id)
                    if (comment.isEmpty()) {
                        comment = ">>${model.comment.num} "
                    } else {
                        comment += " >>${model.comment.num} "
                    }

                    WritingCommentObjApi().setData(giron.id, comment)
                    listener.openCommentWrite(giron.id)
                }
            }, copyCallback = {
                listener.copyClipBoard(model.value)
            })

            return true
        }

        /**
         * static
         */
        companion object {
            private const val TAG = "CommentViewHolder"
        }
    }
}
