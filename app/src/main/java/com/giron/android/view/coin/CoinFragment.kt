package com.giron.android.view.coin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.giron.android.R
import com.giron.android.databinding.FragmentCoinBinding
import com.giron.android.extension.navigate
import com.giron.android.extension.setOption
import com.giron.android.util.RadiusOutlineProvider
import com.giron.android.view.coin.viewModel.CoinViewModel
import com.giron.android.view.parts.listener.OnFragmentActiveListener
import com.giron.android.view.parts.listener.OnMenuSettingListener
import com.giron.android.view.parts.ActionBarMenuSetting

/**
 * CoinFragment
 */
class CoinFragment :
    Fragment(),
    OnMenuSettingListener,
    OnFragmentActiveListener
{
    private lateinit var binding: FragmentCoinBinding
    private lateinit var model: CoinViewModel

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
        binding = FragmentCoinBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

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

        model = ViewModelProvider(this).get(CoinViewModel::class.java)
        model.load()

        binding.coin = model
        binding.coinTopView.outlineProvider = RadiusOutlineProvider(16F)

        setOption()

        binding.advertise.setOnClickListener {
            val action = CoinFragmentDirections.actionCoinFragmentToAdvertisedCoinFragment()
            navigate(action)
        }

        binding.loyalty.setOnClickListener {
            val action = CoinFragmentDirections.actionCoinFragmentToLoyaltyFragment()
            navigate(action)
        }

        binding.loyaltyHistory.setOnClickListener {
            val action = CoinFragmentDirections.actionCoinFragmentToLoyaltyHistoryFragment()
            navigate(action)
        }
    }

    /**
     * active
     */
    override fun active() {
        val act = activity as AppCompatActivity
        act.supportActionBar?.title = context?.getString(R.string.title_mypage)

        model.load()
    }

}