package com.giron.android.model.net

import android.util.Log
import com.giron.android.model.dao.KeyValueObj
import com.giron.android.model.entity.ErrorResponse
import com.giron.android.model.net.realm.KeyValueObjApi
import com.giron.android.util.LogUtils
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 通信クラスの親クラス
 */
abstract class BaseClient {

    protected fun getHttpClient(): OkHttpClient {
        val tokenObj = KeyValueObjApi().getValue(KeyValueObj.Key.GIRON_TOKEN)

        Log.d(TAG, "set token = $tokenObj")

        var token = ""
        if (tokenObj != null) {
            token = tokenObj
        }

        return OkHttpClient().newBuilder().apply {

            connectTimeout(HttpConstants.CONNECT_TIMEOUT_MS, TimeUnit.SECONDS)
            readTimeout(HttpConstants.READ_TIMEOUT_MS, TimeUnit.SECONDS)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            addInterceptor(HeaderInterceptor(token))
        }.build()
    }

    fun getClient(): Retrofit = Retrofit.Builder().apply {

        client(getHttpClient())
        baseUrl(HttpConstants.urlBase())
        addCallAdapterFactory(RxCallAdapterWrapperFactory.create())
        addConverterFactory(GsonConverterFactory.create(Gson()))
    }.build()

    /**
     * 非同期で通信する
     *
     * @param observable 通信ストリーム
     * @param onNext 通信成功後の処理
     * @param onError 通信失敗後の処理
     * @param onComplete 通信完了後の処理
     */
    fun <T> asyncRequest(
        observable: Observable<T>,
        onNext: ((T) -> Unit),
        onError: ((Throwable) -> Unit),
        onComplete: (() -> Unit)

    ): Disposable {

        return observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retryWhen { throwable ->
                throwable.take(HttpConstants.RETRY_COUNT).flatMap {
                    return@flatMap Observable.timer(100, TimeUnit.MILLISECONDS)
                }

            }
            .subscribe({
                LogUtils.d(this::class.java.simpleName, "doOnNext : ${it.toString()}")
                onNext(it)
            }, {
                LogUtils.e(this::class.java.simpleName, "doOnError : ${it.message}")
                onError(it)
            }, {
                LogUtils.d(this::class.java.simpleName, "doOnComplete")
                onComplete()
            })

    }

    /**
     * 非同期で通信する
     *
     * @param single 通信ストリーム
     * @param onSuccess 通信成功後の処理
     * @param onError 通信失敗後の処理
     */
    fun <T> asyncSingleRequest(
        single: Single<T>,
        onSuccess: ((T) -> Unit),
        onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        return single
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(
                    HttpConstants.RETRY_COUNT
            ) {
                // RetrofitException の errorbodyが首都まで着なかった場合はretry
                val ex = it as? RetrofitException
                val body = ex?.mResponse?.errorBody()
                return@retry body == null
            }.subscribe({
                LogUtils.d(this::class.java.simpleName, "doOnSuccess : ${it.toString()}")
                onSuccess(it)
            }, {
                val ex = it as? RetrofitException
                val body = ex?.mResponse?.errorBody()
                body?.let { b ->
                    val error = Gson().fromJson<ErrorResponse>(b.string(), ErrorResponse::class.java)
                    Log.d(TAG, b.string())
                    onError(error)
                    return@subscribe
                }

                LogUtils.e(this::class.java.simpleName, "doOnError : ${it.message}")
                onError(ErrorResponse(400, it.message))
            })
    }

    class HeaderInterceptor(token: String) : Interceptor {

        private val idToken = token

        override fun intercept(chain: Interceptor.Chain): Response {

            return chain.run {
                proceed(
                    request()
                        .newBuilder()
                        .addHeader(HttpConstants.HEADER_AUTH, idToken)
                        .build()
                )
            }
        }
    }

    /**
     * 定義
     */
    companion object {
        private const val TAG = "BaseClient"
    }
}
