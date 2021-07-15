@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.giron.android.databinding.FragmentSettingBinding
import com.giron.android.extension.navigate
import com.giron.android.extension.setOnSafeClickListener
import com.giron.android.extension.setOption
import com.giron.android.model.net.HttpConstants
import com.giron.android.model.net.UserApiClient
import com.giron.android.model.net.realm.UserObjApi
import com.giron.android.util.URL_BASE_LOCAL
import com.giron.android.view.parts.ActionBarMenuSetting
import com.giron.android.view.parts.listener.OnFragmentActiveListener
import com.giron.android.view.parts.listener.OnMenuSettingListener
import com.giron.android.view.setting.viewModel.SettingViewModel

class SettingFragment :
    Fragment(),
    OnMenuSettingListener,
    OnFragmentActiveListener
{
    private lateinit var binding: FragmentSettingBinding

    /**
     * actionBarMenuSetting
     */
    override val actionBarMenuSetting: ActionBarMenuSetting
        get() = ActionBarMenuSetting()

    /**
     * onCreateView
     *
     * @param LayoutInflater
     * @param ViewGroup?
     * @param Bundle?
     *
     * @return View?
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProvider(this).get(SettingViewModel::class.java)
        binding.user = model

        setOption()

        val user = UserObjApi().getData()
        user?.let {
            binding.adsSwitch.isChecked = it.isShowAdvertise
            binding.coinMailSwitch.isChecked = !it.isRejectMailCoin
            binding.magazineMailSwitch.isChecked = !it.isRejectMailMagazine
            binding.campaignMailSwitch.isChecked = !it.isRejectMailCampaign
        }

        // 広告のオンオフ
        binding.adsSwitch.setOnCheckedChangeListener { _, b ->
            if (b) {
                UserApiClient().allowAdvertise({
                    UserObjApi().setShowAdvertise(it.isShowAdvertise)
                }){}
            } else {
                UserApiClient().forbidAdvertise({
                    UserObjApi().setShowAdvertise(it.isShowAdvertise)
                }){}
            }
        }

        binding.coinMailSwitch.setOnCheckedChangeListener { _, b ->
            if (b) {
                UserApiClient().cancelRejectMailCoin({
                    UserObjApi().setRejectMailCoin(it.isRejectMailCoin)
                }){}
            } else {
                UserApiClient().rejectMailCoin({
                    UserObjApi().setRejectMailCoin(it.isRejectMailCoin)
                }){}
            }
        }

        binding.magazineMailSwitch.setOnCheckedChangeListener{ _, b ->
            if (b) {
                UserApiClient().cancelRejectMailMagazine({
                    UserObjApi().setRejectMailMagazine(it.isRejectMailMagazine)
                }){}
            } else {
                UserApiClient().rejectMailMagazine({
                    UserObjApi().setRejectMailMagazine(it.isRejectMailMagazine)
                }){}
            }
        }

        binding.campaignMailSwitch.setOnCheckedChangeListener { _, b ->
            if (b) {
                UserApiClient().cancelRejectMailCampaign({
                    UserObjApi().setRejectMailCampaign(it.isRejectMailCampaign)
                }){}
            } else {
                UserApiClient().rejectMailCampaign({
                    UserObjApi().setRejectMailCampaign(it.isRejectMailCampaign)
                }){}
            }
        }

        // 各種遷移
        binding.eula.setOnSafeClickListener {
            val action = SettingFragmentDirections.actionSettingFragmentToWebFragment()
            action.url = "https://sites.google.com/giron.biz/rule/terms_of_service"
            navigate(action)
        }

        binding.privacyPolicy.setOnSafeClickListener {
            val action = SettingFragmentDirections.actionSettingFragmentToWebFragment()
            action.url = "https://sites.google.com/giron.biz/rule/privacy_policy"
            navigate(action)
        }

        binding.contact.setOnClickListener {
            val action = SettingFragmentDirections.actionSettingFragmentToWebFragment()
            action.url = HttpConstants.urlBase() + "requests/contact/"
            action.required = true
            navigate(action)
        }

        binding.unsubscribe.setOnClickListener {
            val action = SettingFragmentDirections.actionSettingFragmentToWebFragment()
            action.url = HttpConstants.urlBase() + "requests/unsubscribe/"
            action.required = true
            navigate(action)
        }
    }

    /**
     * active
     */
    override fun active() {
        val act = activity as AppCompatActivity
        act.supportActionBar?.title = "Setting"
    }

}