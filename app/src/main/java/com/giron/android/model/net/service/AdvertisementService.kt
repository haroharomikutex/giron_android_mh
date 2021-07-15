@file:Suppress("KDocUnresolvedReference")

package com.giron.android.model.net.service

import com.giron.android.model.entity.AdvertisementEntity
import com.giron.android.model.entity.CoinResponse
import com.giron.android.model.entity.ResultEntity
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * AdvertisementService
 */
interface AdvertisementService {
    /**
     * profile
     *
     * @param Int ID
     */
    @Headers("Accept: application/json")
    @GET("advertisements/{id}/")
    fun retrieve(@Path("id")id: Int): Single<AdvertisementEntity>

    /**
     * imageData
     *
     * @param Int ID
     */
    @Headers("Accept: application/json")
    @GET("advertisements/{id}/image_data/")
    fun imageData(@Path("id")id: Int): Single<ResponseBody>

    @Headers("Accept: application/json")
    @POST("advertisements/{id}/show_advertise/")
    fun showAdvertise(@Path("id")id: Int): Single<CoinResponse>

    @Headers("Accept: application/json")
    @POST("advertisements/{id}/click_advertise/")
    fun clickAdvertise(@Path("id")id: Int): Single<ResultEntity>

    @Headers("Accept: application/json")
    @POST("advertisements/{id}/click_list_advertise/")
    fun clickListAdvertise(@Path("id")id: Int): Single<ResultEntity>
}