package com.giron.android.view.loyalty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.giron.android.GironsNavigationDirections
import com.giron.android.R
import com.giron.android.databinding.FragmentLoyaltyBinding
import com.giron.android.extension.showAlertDialog
import com.giron.android.extension.showError
import com.giron.android.model.net.UserApiClient
import com.giron.android.view.parts.listener.OnFragmentActiveListener
import com.giron.android.view.parts.listener.OnMenuSettingListener
import com.giron.android.view.parts.ActionBarMenuSetting

class LoyaltyFragment: Fragment(), OnMenuSettingListener, OnFragmentActiveListener {
    private lateinit var binding: FragmentLoyaltyBinding

    /**
     * actionBarMenuSetting
     */
    override val actionBarMenuSetting: ActionBarMenuSetting
        get() = ActionBarMenuSetting()

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
        binding = FragmentLoyaltyBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * onViewCreated
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loyaltyRequest.setOnClickListener {
            UserApiClient().sendLoyaltyToken({
                context?.showAlertDialog(it.message)
            }){
                context?.showError(it)
            }
        }

        binding.moreInfo.setOnClickListener {
            val action = GironsNavigationDirections.actionGlobalWebFragment()
            action.url = "https://sites.google.com/giron.biz/rule/loyalty_system"
            findNavController().navigate(action)
        }
    }

    /**
     * active
     */
    override fun active() {
        val act = activity as AppCompatActivity
        act.supportActionBar?.title = context?.getString(R.string.loyalty_request)
    }

}