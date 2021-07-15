@file:Suppress("KDocUnresolvedReference")

package com.giron.android.model.net

import com.giron.android.model.entity.*
import com.giron.android.model.net.service.AdvertisementService
import io.reactivex.disposables.Disposable
import okhttp3.Response
import okhttp3.ResponseBody

/**
 * AdvertisementApiClient
 */
class AdvertisementApiClient : BaseClient() {
    /**
     * retrieve
     * 詳細
     *
     * @param Int ID
     * @param fun success callback
     * @param fun error callback
     *
     * @return Disposable
     */
    fun retrieve(id: Int,
                 onSuccess: ((AdvertisementEntity) -> Unit),
                 onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(AdvertisementService::class.java)
            .retrieve(id)

        return asyncSingleRequest(single, onSuccess, onError)
    }


    /**
     * スプラッシュ画像取得
     *
     * @param String UUID
     * @param fun success callback
     * @param fun error callback
     */
    fun imageData(id: Int,
                  onSuccess: ((ResponseBody) -> Unit),
                  onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(AdvertisementService::class.java)
            .imageData(id)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * スプラッシュ画像取得
     *
     * @param String ID
     * @param fun success callback
     * @param fun error callback
     */
    fun showAdvertise(id: Int,
                  onSuccess: ((CoinResponse) -> Unit),
                  onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(AdvertisementService::class.java)
            .showAdvertise(id)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * スプラッシュ画像取得
     *
     * @param String ID
     * @param fun success callback
     * @param fun error callback
     */
    fun clickAdvertise(id: Int,
                      onSuccess: ((ResultEntity) -> Unit),
                      onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(AdvertisementService::class.java)
            .clickAdvertise(id)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * スプラッシュ画像取得
     *
     * @param String ID
     * @param fun success callback
     * @param fun error callback
     */
    fun clickListAdvertise(id: Int,
                       onSuccess: ((ResultEntity) -> Unit),
                       onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(AdvertisementService::class.java)
            .clickListAdvertise(id)

        return asyncSingleRequest(single, onSuccess, onError)
    }
}