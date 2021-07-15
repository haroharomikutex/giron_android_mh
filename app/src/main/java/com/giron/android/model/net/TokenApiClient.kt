package com.giron.android.model.net

import com.giron.android.model.entity.ErrorResponse
import com.giron.android.model.entity.OnetimeTokenResponse
import com.giron.android.model.net.service.TokenService
import io.reactivex.disposables.Disposable

class TokenApiClient: BaseClient() {
    fun onetime(token: String,
                onSuccess: ((OnetimeTokenResponse) -> Unit),
                onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
                .create(TokenService::class.java)
                .onetime(token)

        return asyncSingleRequest(single, onSuccess, onError)
    }
}