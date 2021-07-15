package com.giron.android.view.advertise.viewModel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.giron.android.model.net.AdvertisementApiClient
import com.giron.android.model.net.realm.UserObjApi
import com.giron.android.view.advertise.listener.AdvertisementListener

/**
 * AdvertiseViewModel
 */
class AdvertiseViewModel: ViewModel() {
    private var id = 0
    private var url = ""
    private lateinit var listener: AdvertisementListener
    var count = MutableLiveData(DisplayCount)
    var countStr = MutableLiveData("$DisplayCount:")
    var unitPriceStr = MutableLiveData("")
    var imageByte = MutableLiveData<ByteArray?>(null)
    var imageError = MutableLiveData<String?>(null)

    /**
     * set
     */
    fun set(
        listener: AdvertisementListener,
        owner: LifecycleOwner,
        id: Int,
        url: String,
        unitPrice: Int
    ) {
        this.id = id
        this.url = url
        this.listener = listener
        count.value = DisplayCount

        unitPriceStr.value ="+$unitPrice"

        count.observe(owner, Observer {
            Log.d(TAG, "count advertise $it")
            countStr.value = "$it:"
        })

        AdvertisementApiClient().imageData(id, {
            try {
                it.bytes()?.let { bytes ->
                    Log.d(TAG, "show image")
                    imageByte.value = bytes
                }
            } catch (e: Exception) {
                imageError.value = e.message
            }
        }){
            imageError.value = it.toString()
        }
    }

    fun show() {
        // 閲覧
        AdvertisementApiClient().showAdvertise(id, {
            UserObjApi().setCoin(it.userCoin)
        }){}
    }

    /**
     * click
     */
    fun click() {
        AdvertisementApiClient().clickAdvertise(id, {}){
            Log.d(TAG, "error add click advertise log advertiseId=$id")
        }
        listener.click(url)
    }

    /**
     * clickBottom()
     */
    fun clickBottom() {
        count.value?.let {
            if(it == 0)
                listener.clickBottom()
        }
    }

    /**
     * static
     */
    companion object {
        const val DisplayCount = 4
        const val TAG = "AdvertiseViewModel"
    }
}