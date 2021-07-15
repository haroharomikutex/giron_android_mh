package com.giron.android.view.login

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.giron.android.R
import com.giron.android.databinding.ActivityEulaBinding
import com.giron.android.model.dao.KeyValueObj
import com.giron.android.model.net.realm.KeyValueObjApi
import com.giron.android.util.EULA_URL
import com.giron.android.util.EULA_VERSION

/**
 * EulaActivity
 */
class EulaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEulaBinding

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_eula)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        }
        else {
            val decor = window.decorView
            @Suppress("DEPRECATION")
            decor.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }

        binding.webview.loadUrl(EULA_URL)

        // 同意
        binding.button.setOnClickListener {
            agree()
        }
    }

    /*
     * agree
     */
    private fun agree() {
        Log.d(TAG, "agree $EULA_VERSION")

        // 規約のバージョンをセットする
        KeyValueObjApi().setValue(KeyValueObj.Key.EULA_VERSION, EULA_VERSION)

        setResult(Activity.RESULT_OK, intent)
        finish()
        overridePendingTransition(R.anim.slide_from_top, R.anim.slide_to_bottom)
    }

    /**
     * static
     */
    companion object {
        private const val TAG = "EulaFragment"
    }
}