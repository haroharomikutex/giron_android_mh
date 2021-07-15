package com.giron.android.model.net

import com.giron.android.BuildConfig

import android.util.Log
import com.giron.android.model.entity.ErrorResponse
import com.giron.android.model.entity.ResultEntity

import io.reactivex.Single
import io.reactivex.disposables.Disposable
import retrofit2.http.*

class ApiClient: BaseClient()  {
    fun checkVersion(onSuccess: ((ResultEntity) -> Unit),
                     onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val version = BuildConfig.VERSION_NAME

        Log.d(TAG, "app version is = $version")
        val single = getClient()
                .create(DefaultApiService::class.java)
                .version(version, "android")

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * 定義
     */
    companion object {
        private const val TAG = "ApiClient"
    }
}

interface DefaultApiService {
    @Headers("Accept: application/json")
    @GET(HttpConstants.END_POINT_CHECK_VERSION)
    fun version(@Query("version")version: String,
                @Query("os") os: String): Single<ResultEntity>
}