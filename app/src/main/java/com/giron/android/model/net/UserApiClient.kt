@file:Suppress("KDocUnresolvedReference")

package com.giron.android.model.net

import com.giron.android.BuildConfig
import com.giron.android.model.dao.TimeStampObj
import com.giron.android.model.entity.*
import com.giron.android.model.net.realm.TimeStampObjApi
import com.giron.android.model.net.service.UserService
import io.reactivex.disposables.Disposable
import okhttp3.Response

/**
 * UserApiClient
 */
class UserApiClient : BaseClient() {
    /**
     * サインアップ
     *
     * @param String Firebase token
     * @param fun success callback
     * @param fun error callback
     */
    fun signUp(idToken: String?,
               onSuccess: ((UserEntity) -> Unit),
               onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val versionName = BuildConfig.VERSION_NAME
        val single = getClient()
                .create(UserService::class.java)
                .signUp(idToken, "android", versionName)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * サインイン
     *
     * @param fun success callback
     * @param fun error callback
     */
    fun signIn(onSuccess: ((UserEntity) -> Unit),
               onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val versionName = BuildConfig.VERSION_NAME
        val single = getClient()
                .create(UserService::class.java)
                .signIn("android", versionName)

        return asyncSingleRequest(single, onSuccess, onError)
    }


    /**
     * デバイス登録トークンのセット
     *
     * @param String Firebase token
     * @param fun success callback
     * @param fun error callback
     */
    fun setDeviceToken(token: String,
                       onSuccess: ((ResultEntity) -> Unit),
                       onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
                .create(UserService::class.java)
                .setDeviceToken(token)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * ユーザー画像取得
     *
     * @param String UUID
     * @param fun success callback
     * @param fun error callback
     */
    fun imageData(uuid: String,
                   onSuccess: ((Response) -> Unit),
                   onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
                .create(UserService::class.java)
                .imageData(uuid)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * batchCount
     *
     * @param fun success callback
     * @param fun error callback
     */
    fun batchCount(
        onSuccess: ((BatchCountResponseEntity) -> Unit),
        onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val data = TimeStampObjApi().getAllData()

        val single = getClient()
            .create(UserService::class.java)
            .batchCount(
                data[TimeStampObj.Key.GIRON.value],
                data[TimeStampObj.Key.NOTICE.value],
                data[TimeStampObj.Key.INFORMATION.value],
                data[TimeStampObj.Key.REWORDED.value],
                data[TimeStampObj.Key.CoinByAdvertisement.value]
            )

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * myProfile
     *
     * @param fun success callback
     * @param fun error callback
     */
    fun myProfile(
        onSuccess: ((MyProfileResponseEntity) -> Unit),
        onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(UserService::class.java)
            .myProfile()

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * myProfile
     *
     * @param String UUID
     * @param fun success callback
     * @param fun error callback
     */
    fun profile(
        uuid: String,
        onSuccess: ((UserProfileEntity) -> Unit),
        onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(UserService::class.java)
            .profile(uuid)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * editProfile
     *
     * @param String Name
     * @param String Profile
     * @param fun success callback
     * @param fun error callback
     */
    fun editProfile(
        name: String,
        profile: String,
        onSuccess: ((MyProfileResponseEntity) -> Unit),
        onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(UserService::class.java)
            .editProfile(name, profile)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * getCoinLimit
     *
     * @param fun success callback
     * @param fun error callback
     */
    fun getCoinLimit(
        onSuccess: ((CoinLimitResponseEntity) -> Unit),
        onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(UserService::class.java)
            .getCoinLimit()

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * sendLoyaltyToken
     *
     * @param fun success callback
     * @param fun error callback
     */
    fun sendLoyaltyToken(
        onSuccess: ((MessageResultEntity) -> Unit),
        onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(UserService::class.java)
            .sendLoyaltyToken()

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * loyaltyRequests
     *
     *
     * @param Int offset
     * @param fun success callback
     * @param fun error callback
     */
    fun loyaltyRequests(
        offset: Int = 0,
        onSuccess: ((LoyaltyRequestsEntity) -> Unit),
        onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(UserService::class.java)
            .loyaltyRequests(offset)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * advertisedCoin
     *
     * @param Int offset
     * @param fun success callback
     * @param fun error callback
     */
    fun advertisedCoin(
        offset: Int = 0,
        onSuccess: ((AdvertisedCoinsEntity) -> Unit),
        onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(UserService::class.java)
            .advertisedCoin(offset)

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * allowAdvertise
     *
     * @param fun success callback
     * @param fun error callback
     */
    fun allowAdvertise(
        onSuccess: ((AdvertiseResultEntity) -> Unit),
        onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(UserService::class.java)
            .allowAdvertise()

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * forbidAdvertise
     *
     * @param fun success callback
     * @param fun error callback
     */
    fun forbidAdvertise(
        onSuccess: ((AdvertiseResultEntity) -> Unit),
        onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(UserService::class.java)
            .forbidAdvertise()

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * rejectMailCoin
     *
     * @param fun success callback
     * @param fun error callback
     */
    fun rejectMailCoin(
        onSuccess: ((RejectMailCoinEntity) -> Unit),
        onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(UserService::class.java)
            .rejectMailCoin()

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * cancelRejectMailCoin
     *
     * @param fun success callback
     * @param fun error callback
     */
    fun cancelRejectMailCoin(
        onSuccess: ((RejectMailCoinEntity) -> Unit),
        onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(UserService::class.java)
            .cancelRejectMailCoin()

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * rejectMailMagazine
     *
     * @param fun success callback
     * @param fun error callback
     */
    fun rejectMailMagazine(
        onSuccess: ((RejectMailMagazineEntity) -> Unit),
        onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(UserService::class.java)
            .rejectMailMagazine()

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * rejectMailMagazine
     *
     * @param fun success callback
     * @param fun error callback
     */
    fun cancelRejectMailMagazine(
        onSuccess: ((RejectMailMagazineEntity) -> Unit),
        onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(UserService::class.java)
            .cancelRejectMailMagazine()

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * rejectMailCampaign
     *
     * @param fun success callback
     * @param fun error callback
     */
    fun rejectMailCampaign(
        onSuccess: ((RejectMailCampaignEntity) -> Unit),
        onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(UserService::class.java)
            .rejectMailCampaign()

        return asyncSingleRequest(single, onSuccess, onError)
    }

    /**
     * cancelRejectMailCampaign
     *
     * @param fun success callback
     * @param fun error callback
     */
    fun cancelRejectMailCampaign(
        onSuccess: ((RejectMailCampaignEntity) -> Unit),
        onError: ((ErrorResponse) -> Unit)
    ): Disposable {
        val single = getClient()
            .create(UserService::class.java)
            .cancelRejectMailCampaign()

        return asyncSingleRequest(single, onSuccess, onError)
    }


}