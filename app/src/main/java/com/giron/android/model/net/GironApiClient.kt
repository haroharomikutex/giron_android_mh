@file:Suppress("KDocUnresolvedReference")

package com.giron.android.model.net

import com.giron.android.model.entity.*
import com.giron.android.model.net.service.GironService
import com.giron.android.view.giron.detail.viewModel.EmotionType
import io.reactivex.disposables.Disposable

/**
 * GironApiClient
 */
class GironApiClient : BaseClient() {
    /**
     * search
     * 検索
     *
     * @param String 検索ワード
     * @param String 検索タイプ
     * @param Int? offset
     * @param Int? limit
     * @param fun success callback
     * @param fun error callback
     *
     * @return Disposable
     */
    fun search(word: String,
               type: String,
               offset: Int? = null,
               limit: Int? = null,
               onSuccess: ((GironsEntity) -> Unit),
               onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
                .create(GironService::class.java)
                .search(word, type, offset, limit)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * retrieve
     * 詳細
     *
     * @param Int GironID
     * @param Int? Comment Number
     * @param fun success callback
     * @param fun error callback
     *
     * @return Disposable
     */
    fun retrieve(id: Int,
                 num: Int?,
                 onSuccess: ((GironDetailEntity) -> Unit),
                 onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        var single = getClient()
                .create(GironService::class.java)
                .retrieveZero(id)

        num?.let {
            single = getClient()
                    .create(GironService::class.java)
                    .retrieve(id, it)
        }

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * tags
     * タグ
     *
     * @param Int GironID
     * @param fun success callback
     * @param fun error callback
     *
     * @return Disposable
     */
    fun tags(id: Int,
             onSuccess: ((TagsEntity) -> Unit),
             onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
                .create(GironService::class.java)
                .tags(id)

        return asyncSingleRequest(single, onSuccess, onError)
    }


    /**
     * commentsByNum
     * numberからコメントを取得する
     *
     * @param Int GironID
     * @param Int? Comment Number
     * @param fun success callback
     * @param fun error callback
     *
     * @return Disposable
     */
    fun commentsByNum(id: Int,
                      num: Int,
                      onSuccess: (CommentsEntity) -> Unit,
                      onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
                .create(GironService::class.java)
                .comments(id, num, 0, "")

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * commentsByPage
     * page Noからコメントを取得する
     *
     * @param Int GironID
     * @param Int Page Number
     * @param fun success callback
     * @param fun error callback
     *
     * @return Disposable
     */
    fun commentsByPage(id: Int,
                       page: Int,
                       onSuccess: (CommentsEntity) -> Unit,
                       onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
                .create(GironService::class.java)
                .comments(id, 0, page, "")

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * emotion
     * エモーションの追加
     *
     * @param Int GironID
     * @param EmotionType エモーションのタイプ
     * @param fun success callback
     * @param fun error callback
     *
     * @return Disposable
     */
    fun emotion(id: Int,
                type: EmotionType,
                onSuccess: ((EmotionsEntity) -> Unit),
                onError: ((ErrorResponse) -> Unit)
    ): Disposable {

        val single = getClient()
                .create(GironService::class.java)
                .emotion(id, type.num)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * emotion
     * エモーションの削除
     *
     * @param Int GironID
     * @param EmotionType エモーションのタイプ
     * @param fun success callback
     * @param fun error callback
     *
     * @return Disposable
     */
    fun cancelEmotion(id: Int,
                      type: EmotionType,
                      onSuccess: ((EmotionsEntity) -> Unit),
                      onError: ((ErrorResponse) -> Unit)
    ): Disposable {

        val single = getClient()
                .create(GironService::class.java)
                .cancelEmotion(id, type.num)

        return asyncSingleRequest(single, onSuccess, onError)
    }


    /**
     * reword
     * リワードの追加
     *
     * @param Int GironID
     * @param Int APIから取得したリワードID
     * @param fun success callback
     * @param fun error callback
     *
     * @return Disposable
     */
    fun reword(id: Int,
               rewordId: Int,
               onSuccess: ((CoinResponse) -> Unit),
               onError: ((ErrorResponse) -> Unit)
    ): Disposable {

        val single = getClient()
                .create(GironService::class.java)
                .reword(id, rewordId)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * create
     * Giron作成
     *
     * @param
     */
    fun create(createGironEntity: CreateGironEntity,
               onSuccess: ((GironDetailEntity) -> Unit),
               onError: ((ErrorResponse) -> Unit)) : Disposable {
        val single = getClient()
            .create(GironService::class.java)
            .create(createGironEntity)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * editTag
     * タグの更新
     *
     * @param Int GironID
     * @param ArrayList<TagEditEntity> 更新するタグ
     * @param fun success callback
     * @param fun error callback
     *
     * @return Disposable
     */
    fun editTag(id: Int,
                tags: TagsEditEntity,
                onSuccess: ((TagsEntity) -> Unit),
                onError: ((ErrorResponse) -> Unit)) : Disposable {
        val single = getClient()
                .create(GironService::class.java)
                .editTag(id, tags)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * show
     * 詳細
     *
     * @param Int GironID
     * @param fun success callback
     * @param fun error callback
     *
     * @return Disposable
     */
    fun createComment(id: Int,
                      comment: String,
                      onSuccess: ((CreateCommentEntity) -> Unit),
                      onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
                .create(GironService::class.java)
                .createComment(id, comment)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * new_comment
     * 新規コメントを取得する
     *
     * @param Int GironID
     * @param Int offset
     * @param fun success callback
     * @param fun error callback
     *
     * @return Disposable
     */
    fun newComment(id: Int,
                   offset: Int,
                   onSuccess: ((CommentsEntity) -> Unit),
                   onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
                .create(GironService::class.java)
                .newComments(id, offset)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * getPopularityWord
     * 最近の人気検索ワードを取得する
     *
     * @param String
     * @param fun success callback
     * @param fun error callback
     *
     * @return Disposable
     */
    fun getPopularityWord(word: String,
                          onSuccess: ((SearchWordsEntity) -> Unit),
                          onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
                .create(GironService::class.java)
                .getPopularityWord(word)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * getPopularityWord
     * 検索候補を取得する
     *
     * @param String
     * @param fun success callback
     * @param fun error callback
     *
     * @return Disposable
     */
    fun searchCandidate(word: String,
                          onSuccess: ((SearchWordsEntity) -> Unit),
                          onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
                .create(GironService::class.java)
                .searchCandidate(word)

        return asyncSingleRequest(single, onSuccess, onError)
    }
}