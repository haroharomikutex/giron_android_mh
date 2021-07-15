package com.giron.android.model.net.service

import com.giron.android.model.entity.TagCandidateEntity
import io.reactivex.Single
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * TagService
 */
interface TagService {
    /**
     * recommends
     *
     * @return Single<TagCandidateEntity>
     */
    @Headers("Accept: application/json")
    @GET("tags/recommends/")
    fun recommends(): Single<TagCandidateEntity>
}