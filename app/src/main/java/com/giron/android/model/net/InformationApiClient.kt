@file:Suppress("KDocUnresolvedReference")

package com.giron.android.model.net

import com.giron.android.model.entity.ErrorResponse
import com.giron.android.model.entity.InformationsEntity
import com.giron.android.model.entity.LoyaltyRequestsEntity
import com.giron.android.model.net.service.InformationService
import com.giron.android.model.net.service.UserService
import io.reactivex.disposables.Disposable

/**
 * InformationApiClient
 */
class InformationApiClient : BaseClient() {
    /**
     * list
     *
     * @param Int Offset
     * @param fun success callback
     * @param fun error callback
     */
    fun list(
        offset: Int = 0,
        onSuccess: ((InformationsEntity) -> Unit),
        onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(InformationService::class.java)
            .list(offset)

        return asyncSingleRequest(single, onSuccess, onError)
    }
}