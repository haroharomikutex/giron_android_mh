@file:Suppress("KDocUnresolvedReference")

package com.giron.android.model.net.service

import com.giron.android.model.entity.*
import io.reactivex.Single
import okhttp3.Response
import retrofit2.http.*

/**
 * UserService
 */
interface UserService {
    /**
     * signUp
     *
     * @param String Firebase token
     * @param String OS
     * @param String version
     *
     * @return Single<UserEntity>
     */
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("users/sign_up/")
    fun signUp(@Field("token") idToken: String?,
               @Field("os") os: String?,
               @Field("version") version: String?): Single<UserEntity>

    /**
     * signIn
     *
     * @param String OS
     * @param String version
     *
     * @return Single<UserEntity>
     */
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("users/sign_in/")
    fun signIn(@Field("os") os: String?,
               @Field("version") version: String?): Single<UserEntity>
    /**
     * signIn
     *
     * @param String token
     *
     * @return Single<ResultEntity>
     */
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("users/set_device_token/")
    fun setDeviceToken(@Field("token") idToken: String?): Single<ResultEntity>

    /**
     * imageData
     *
     * @param String UUID
     */
    @Headers("Accept: application/json")
    @GET("users/{uuid}/image_data/")
    fun imageData(@Path("uuid")uuid: String): Single<Response>

    /**
     * batchCount
     *
     * @param String giron
     * @param String notice
     * @param String information
     * @param String reworded
     * @param String advertisement Coin History
     */
    @Headers("Accept: application/json")
    @GET("users/batch_count/")
    fun batchCount(
        @Query("giron")giron: String?,
        @Query("notice")notice: String?,
        @Query("information")information: String?,
        @Query("reworded")reworded: String?,
        @Query("coin_by_advertisement")advertisementCoin: String?
    ): Single<BatchCountResponseEntity>

    /**
     * myProfile
     */
    @Headers("Accept: application/json")
    @GET("users/my_profile/")
    fun myProfile(): Single<MyProfileResponseEntity>

    /**
     * profile
     */
    @Headers("Accept: application/json")
    @GET("users/{uuid}/")
    fun profile(@Path("uuid")uuid: String): Single<UserProfileEntity>

    /**
     * editProfile
     *
     * @param String Name
     * @param String Profile
     */
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("users/profile/")
    fun editProfile(
        @Field("name")name: String,
        @Field("profile")profile: String
    ): Single<MyProfileResponseEntity>

    /**
     * get_coin_limit
     */
    @Headers("Accept: application/json")
    @GET("users/get_coin_limit/")
    fun getCoinLimit(): Single<CoinLimitResponseEntity>

    /**
     * sendLoyaltyToken
     */
    @Headers("Accept: application/json")
    @POST("users/send_loyalty_token/")
    fun sendLoyaltyToken(): Single<MessageResultEntity>

    /**
     * loyaltyRequests
     *
     * @param Int offset
     */
    @Headers("Accept: application/json")
    @GET("users/loyalty_requests/")
    fun loyaltyRequests(@Query("offset")offset: Int): Single<LoyaltyRequestsEntity>

    /**
     * advertisedCoin
     *
     * @param Int offset
     */
    @Headers("Accept: application/json")
    @GET("users/get_advertised_coin/")
    fun advertisedCoin(@Query("offset")offset: Int): Single<AdvertisedCoinsEntity>

    /**
     * allowAdvertise
     */
    @Headers("Accept: application/json")
    @POST("users/allow_advertise/")
    fun allowAdvertise(): Single<AdvertiseResultEntity>

    /**
     * forbidAdvertise
     */
    @Headers("Accept: application/json")
    @POST("users/forbid_advertise/")
    fun forbidAdvertise(): Single<AdvertiseResultEntity>

    /**
     * rejectMailCoin
     */
    @Headers("Accept: application/json")
    @POST("users/reject_mail_coin/")
    fun rejectMailCoin(): Single<RejectMailCoinEntity>

    /**
     * cancelRejectMailCoin
     */
    @Headers("Accept: application/json")
    @POST("users/cancel_reject_mail_coin/")
    fun cancelRejectMailCoin(): Single<RejectMailCoinEntity>

    /**
     * rejectMailMagazine
     */
    @Headers("Accept: application/json")
    @POST("users/reject_mail_magagine/")
    fun rejectMailMagazine(): Single<RejectMailMagazineEntity>

    /**
     * cancelRejectMailMagazine
     */
    @Headers("Accept: application/json")
    @POST("users/cancel_reject_mail_magagine/")
    fun cancelRejectMailMagazine(): Single<RejectMailMagazineEntity>

    /**
     * rejectMailCampaign
     */
    @Headers("Accept: application/json")
    @POST("users/reject_mail_campaign/")
    fun rejectMailCampaign(): Single<RejectMailCampaignEntity>

    /**
     * cancelRejectMailCampaign
     */
    @Headers("Accept: application/json")
    @POST("users/cancel_reject_mail_campaign/")
    fun cancelRejectMailCampaign(): Single<RejectMailCampaignEntity>
}