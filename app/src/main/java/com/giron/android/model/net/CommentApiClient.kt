package com.giron.android.model.net

import com.giron.android.model.entity.CoinResponse
import com.giron.android.model.entity.EmotionsEntity
import com.giron.android.model.entity.ErrorResponse
import com.giron.android.view.giron.detail.viewModel.EmotionType
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import retrofit2.http.*

class CommentApiClient: BaseClient() {
    /**
     * エモーション
     */
    fun emotion(id: Int,
                type: EmotionType,
                onSuccess: ((EmotionsEntity) -> Unit),
                onError: ((ErrorResponse) -> Unit)
    ): Disposable {

        val single = getClient()
                .create(CommentService::class.java)
                .emotion(id, type.num)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * エモーション
     */
    fun cancelEmotion(id: Int,
                      type: EmotionType,
                      onSuccess: ((EmotionsEntity) -> Unit),
                      onError: ((ErrorResponse) -> Unit)
    ): Disposable {

        val single = getClient()
                .create(CommentService::class.java)
                .cancelEmotion(id, type.num)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * エモーション
     */
    fun reword(id: Int,
               reword_id: Int,
               onSuccess: ((CoinResponse) -> Unit),
               onError: ((ErrorResponse) -> Unit)
    ): Disposable {

        val single = getClient()
                .create(CommentService::class.java)
                .reword(id, reword_id)

        return asyncSingleRequest(single, onSuccess, onError)
    }
}

interface CommentService {
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("comments/{id}/emotion/")
    fun emotion(@Path("id") id: Int,
                @Field("type") type: Int): Single<EmotionsEntity>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("comments/{id}/cancel_emotion/")
    fun cancelEmotion(@Path("id") id: Int,
                      @Field("type") type: Int): Single<EmotionsEntity>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("comments/{id}/reword/")
    fun reword(@Path("id") id: Int,
               @Field("reword_id") rewordId: Int): Single<CoinResponse>
}