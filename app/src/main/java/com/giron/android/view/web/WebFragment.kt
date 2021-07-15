package com.giron.android.view.web

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.giron.android.databinding.FragmentWebBinding
import com.giron.android.extension.setOption
import com.giron.android.model.dao.KeyValueObj
import com.giron.android.model.net.realm.KeyValueObjApi
import com.giron.android.view.parts.ActionBarMenuSetting
import com.giron.android.view.parts.listener.OnMenuSettingListener

/**
 * WebFragment
 * Web画面を開く
 */
class WebFragment: Fragment(), OnMenuSettingListener {
    private val args : WebFragmentArgs by navArgs()
    private lateinit var binding: FragmentWebBinding

    /**
     * actionBarMenuSetting
     */
    override val actionBarMenuSetting: ActionBarMenuSetting
        get() = ActionBarMenuSetting(navVisible = false)

    /**
     * onCreateView
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return View?
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * onViewCreated
     *
     * @param view
     * @param savedInstanceState
     */
    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val act = activity as AppCompatActivity
        act.supportActionBar?.title = ""

        setOption()

        if (args.required) {
            val tokenObj = KeyValueObjApi().getValue(KeyValueObj.Key.GIRON_TOKEN) ?: ""
            binding.main.settings.javaScriptEnabled = true
            binding.main.postUrl(args.url, "giron_token=$tokenObj".toByteArray())
        }
        else {
            binding.main.loadUrl(args.url)
        }

        // タイトルのセット
        binding.main.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                act.supportActionBar?.title = view?.title
            }
        }
    }
}