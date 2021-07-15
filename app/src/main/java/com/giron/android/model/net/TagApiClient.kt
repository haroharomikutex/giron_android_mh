package com.giron.android.model.net

import com.giron.android.model.entity.ErrorResponse
import com.giron.android.model.entity.TagCandidateEntity
import com.giron.android.model.net.service.TagService
import io.reactivex.disposables.Disposable

class TagApiClient: BaseClient() {
    /**
     * よく使われているタグ
     * @return TagCandidateEntity タグ一覧
     */
    fun hot(onSuccess: ((TagCandidateEntity) -> Unit),
            onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
                .create(TagService::class.java)
                .recommends()

        return asyncSingleRequest(single, onSuccess, onError)
    }
}