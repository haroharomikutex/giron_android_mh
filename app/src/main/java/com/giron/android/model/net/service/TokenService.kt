package com.giron.android.model.net.service

import com.giron.android.model.entity.OnetimeTokenResponse
import io.reactivex.Single
import retrofit2.http.*

/**
 * TokenService
 */
interface TokenService {
    @Headers("Accept: application/json")
    @POST("token/{token}/onetime/")
    fun onetime(@Path("token")token: String): Single<OnetimeTokenResponse>
}