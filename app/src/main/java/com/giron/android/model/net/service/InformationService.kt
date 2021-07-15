@file:Suppress("KDocUnresolvedReference")

package com.giron.android.model.net.service

import com.giron.android.model.entity.InformationsEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * InformationService
 */
interface InformationService {
    /**
     * list
     *
     * @param Int Offset
     *
     * @return Single<InformationsEntity>
     */
    @Headers("Accept: application/json")
    @GET("informations/")
    fun list(@Query("offset")offset: Int): Single<InformationsEntity>
}