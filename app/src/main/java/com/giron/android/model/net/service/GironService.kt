@file:Suppress("KDocUnresolvedReference")

package com.giron.android.model.net.service

import com.giron.android.model.entity.*
import io.reactivex.Single
import retrofit2.http.*

/**
 * GironService
 */
interface GironService {
    /**
     * search
     *
     * @param String word
     * @param String type
     * @param Int offset
     * @param Int limit
     *
     * @return Single<GironsEntity>
     */
    @Headers("Accept: application/json")
    @GET("girons/search/")
    fun search(@Query("word") word: String,
               @Query("type") type: String,
               @Query("offset") offset: Int?,
               @Query("limit") limit: Int?): Single<GironsEntity>

    /**
     * retrieve
     *
     * @param Int ID
     * @param Int num
     *
     * @return Single<GironDetailEntity>
     */
    @Headers("Accept: application/json")
    @GET("girons/{id}/")
    fun retrieve(@Path("id") id: Int,
                 @Query("num") num: Int): Single<GironDetailEntity>

    @Headers("Accept: application/json")
    @GET("girons/{id}/")
    fun retrieveZero(@Path("id") id: Int): Single<GironDetailEntity>

    @Headers("Accept: application/json")
    @GET("girons/{id}/tags/")
    fun tags(@Path("id") id: Int): Single<TagsEntity>

    @Headers("Accept: application/json")
    @GET("girons/{id}/comments/")
    fun comments(@Path("id") id: Int,
                 @Query("num") num: Int,
                 @Query("page") page: Int,
                 @Query("timestamp") timestamp: String): Single<CommentsEntity>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("girons/{id}/emotion/")
    fun emotion(@Path("id") id: Int,
                @Field("type") type: Int): Single<EmotionsEntity>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("girons/{id}/cancel_emotion/")
    fun cancelEmotion(@Path("id") id: Int,
                      @Field("type") type: Int): Single<EmotionsEntity>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("girons/{id}/reword/")
    fun reword(@Path("id") id: Int,
               @Field("reword_id") rewordId: Int): Single<CoinResponse>

    @Headers("Accept: application/json")
    @POST("girons/{id}/edit_tag/")
    fun editTag(@Path("id") id: Int,
                @Body tags: TagsEditEntity): Single<TagsEntity>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("girons/{id}/create_comment/")
    fun createComment(@Path("id") id: Int,
                      @Field("comment") comment: String): Single<CreateCommentEntity>

    @Headers("Accept: application/json")
    @GET("girons/{id}/new_comments/")
    fun newComments(@Path("id") id: Int,
                    @Query("offset") offset: Int): Single<CommentsEntity>

    @Headers("Accept: application/json")
    @GET("girons/get_popularity_word/")
    fun getPopularityWord(@Query("word") word: String): Single<SearchWordsEntity>

    @Headers("Accept: application/json")
    @GET("girons/search_candidate/")
    fun searchCandidate(@Query("word") word: String): Single<SearchWordsEntity>

    @Headers("Accept: application/json")
    @POST("girons/")
    fun create( @Body createGironEntity: CreateGironEntity): Single<GironDetailEntity>
}