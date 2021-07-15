@file:Suppress("KDocUnresolvedReference")

package com.giron.android.model.net

import com.giron.android.model.entity.ErrorResponse
import com.giron.android.model.entity.NoticeEntity
import com.giron.android.model.entity.ResultEntity
import com.giron.android.model.net.service.NoticeService
import io.reactivex.disposables.Disposable

/**
 * NoticeApiClient
 */
class NoticeApiClient: BaseClient() {
    /**
     * list
     *
     * @param fun success callback
     * @param fun error callback
     */
    fun list(offset: Int = 0,
             onSuccess: ((ArrayList<NoticeEntity>) -> Unit),
             onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(NoticeService::class.java)
            .list(offset)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    fun click(id: Int,
              onSuccess: ((ResultEntity) -> Unit),
              onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(NoticeService::class.java)
            .click(id)

        return asyncSingleRequest(single, onSuccess, onError)
    }
}