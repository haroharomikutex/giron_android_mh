package com.giron.android.model.net.service

import com.giron.android.model.entity.NoticeEntity
import com.giron.android.model.entity.ResultEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * NoticeService
 */
interface NoticeService {
    /**
     * list
     *
     * @return Single<ArrayList<NoticeEntity>>
     */
    @Headers("Accept: application/json")
    @GET("notices/")
    fun list(@Query("offset") offset: Int): Single<ArrayList<NoticeEntity>>

    /**
     * click
     *
     * @param id ID
     *
     * @return
     */
    @Headers("Accept: application/json")
    @GET("notices/{id}/")
    fun click(@Path("id") id: Int): Single<ResultEntity>
}