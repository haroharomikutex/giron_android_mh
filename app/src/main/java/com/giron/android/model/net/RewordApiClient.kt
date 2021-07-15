package com.giron.android.model.net

import com.giron.android.model.entity.ErrorResponse
import com.giron.android.model.entity.RewordEntity
import com.giron.android.model.entity.RewordsEntity
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import retrofit2.http.GET
import retrofit2.http.Headers

class RewordApiClient: BaseClient() {
    fun list(
            onSuccess: ((ArrayList<RewordEntity>) -> Unit),
            onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
                .create(RewordService::class.java)
                .list()

        return asyncSingleRequest(single, onSuccess, onError)
    }
}

interface RewordService {
    @Headers("Accept: application/json")
    @GET("rewords/")
    fun list(): Single<ArrayList<RewordEntity>>
}