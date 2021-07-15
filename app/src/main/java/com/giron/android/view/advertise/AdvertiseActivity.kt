package com.giron.android.view.advertise

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.giron.android.R
import com.giron.android.databinding.ActivityAdvertiseBinding
import com.giron.android.extension.setByteArray
import com.giron.android.util.RadiusOutlineProvider
import com.giron.android.view.advertise.listener.AdvertisementListener
import com.giron.android.view.advertise.viewModel.AdvertiseViewModel

/**
 * AdvertiseActivity
 */
class AdvertiseActivity : AppCompatActivity(), AdvertisementListener {
    private lateinit var binding: ActivityAdvertiseBinding

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model = ViewModelProvider(this).get(AdvertiseViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_advertise)
        binding.advertisement = model

        binding.count.outlineProvider = RadiusOutlineProvider(20F)

        model.set(
            this,
            this,
            intent.getIntExtra("id", 0),
            intent.getStringExtra("url") ?: "",
            intent.getIntExtra("unitPrice", 0)
        )

        // MutableLiveDataでのalpha値変更はタイミングの問題か反映されない。
        // 直接変更の必要あり。
        binding.main.alpha = 0f

        val timer = object: CountDownTimer(AdvertiseViewModel.DisplayCount * 1000.toLong(), 100) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val count = (millisUntilFinished / 1000).toInt()
                model.count.value = count
                binding.countStr.text = "$count:"
                Log.d(TAG, "count advertise $count")
            }

            override fun onFinish() {
                model.count.value = 0
                finishTimer()
            }
        }

        // 画像をセットして表示
        model.imageByte.observe(this, {
            Log.d(TAG, "change imageByte")
            it?.let { bytes ->
                Log.d(TAG, "set image")
                fadeIn()
                binding.image.setByteArray(bytes)
                model.show()
                timer.start()
            }
        })

        // エラー時は終了
        model.imageError.observe(this, {
            it?.let {mes ->
                Log.d(TAG, "Error: $mes")
                execFinish()
            }
        })
    }

    private fun fadeIn() {
        binding.main.alpha = 1f
        val animation= AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.main.startAnimation(animation)
    }

    private fun execFinish() {
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    /**
     * finishTimer
     */
    override fun finishTimer() {
        execFinish()
    }

    /**
     * click
     *
     * @param url
     */
    override fun click(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    /**
     * clickBottom
     */
    override fun clickBottom() {
        execFinish()
    }


    /**
     * static
     */
    companion object {
        private const val TAG = "AdvertiseActivity"
    }
}