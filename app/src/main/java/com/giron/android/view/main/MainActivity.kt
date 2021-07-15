package com.giron.android.view.main

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStore
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.giron.android.R
import com.giron.android.databinding.ActivityMainBinding
import com.giron.android.extension.setOption
import com.giron.android.extension.showAlertDialog
import com.giron.android.extension.transition
import com.giron.android.model.dao.OnetimeToken
import com.giron.android.model.dao.Transition
import com.giron.android.model.entity.AdvertisementEntity
import com.giron.android.model.net.AdvertisementApiClient
import com.giron.android.model.net.TokenApiClient
import com.giron.android.model.net.UserApiClient
import com.giron.android.model.net.realm.BatchCountObjApi
import com.giron.android.model.net.realm.RewordObjApi
import com.giron.android.model.net.realm.TagObjApi
import com.giron.android.model.net.realm.UserObjApi
import com.giron.android.view.advertise.AdvertiseActivity
import com.giron.android.view.parts.ActionBarMenuSetting
import com.giron.android.view.parts.SharedViewModelStoreOwner
import com.giron.android.view.parts.listener.*
import com.google.android.material.bottomnavigation.BottomNavigationView


/**
 * MainActivity
 * 根幹となるActivity
 * 各画面遷移はこのActivityを通して行う
 */
class MainActivity : AppCompatActivity(), SharedViewModelStoreOwner {
    private var binding: ActivityMainBinding? = null
    private var menu: Menu? = null
    private var currentTag: String = ""

    /**
     * topFragment
     * The Fragment of the selected tab
     */
    val topFragment: Fragment?
        get() {
            val topManager = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager
            return topManager?.findFragmentByTag(currentTag)
        }

    val topFragmentListener: TopFragmentListener?
        get() {
            val top = topFragment
            if (top is TopFragmentListener)
                return top
            return null
        }

    /**
     * currentFragment
     */
    private val currentFragment: Fragment?
        get() {
            val top = topFragment
            if (top is TopFragmentListener)
                return top.currentFragment

            return topFragment
        }

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        this.binding = binding

        // 必要なデータをRealmに保存する
        RewordObjApi().setData(this, packageName)
        TagObjApi().setData()

        // ビュー
        val navView: BottomNavigationView = binding.navView
        val navController = Navigation.findNavController(supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.requireView()!!)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                    R.id.navigation_home, R.id.navigation_notifications, R.id.navigation_dashboard
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            Log.d(TAG, "onChangeListener label = ${destination.label})")
            currentTag = destination.id.toString()

            // Callback when Fragment is active
            val fragment = currentFragment
            fragment?.setOption()

            // refresh Batch Count
            refreshBatch()
        }

        // 処理
        checkAdvertise()
    }

    /**
     * checkAdvertise
     */
    private fun checkAdvertise() {
        UserObjApi().getData()?.advertiseId?.let { id ->
            AdvertisementApiClient().retrieve(id, {
                showAdvertise(it)
            }){}
        } ?:run {
            finishAdvertise()
        }
    }

    /**
     * showAdvertise
     *
     * @param entity
     */
    private fun showAdvertise(entity: AdvertisementEntity) {
        val intent = Intent(application, AdvertiseActivity::class.java)
        intent.putExtra("id", entity.id)
        intent.putExtra("unitPrice", entity.unitPrice)
        intent.putExtra("url", entity.url)
        startActivityForResult(intent, ADVERTISE_RESULT_CODE)
    }

    /**
     * showAdvertise
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            ADVERTISE_RESULT_CODE -> finishAdvertise()
        }
    }

    /**
     * finishAdvertise
     */
    private fun finishAdvertise() {
        Log.d(TAG, "finishAdvertise")
        checkOnetimeToken()
    }

    /**
     * checkOnetimeToken
     */
    private fun checkOnetimeToken() {
        Log.d(TAG, "checkOnetimeToken")

        val extra = intent.getSerializableExtra(OnetimeToken.KEY) as? OnetimeToken
        extra?.let { onetimeToken ->
            Log.d(TAG, "onetimeToken: ${onetimeToken.token}")
            requestOnetimeToken(onetimeToken.token)
        }
    }

    /**
     * requestOnetimeToken
     *
     * @param token
     */
    private fun requestOnetimeToken(token: String) {
        Log.d(TAG, "requestOnetimeToken")

        TokenApiClient().onetime(token, {
            showAlertDialog(it.message)
        }, {})
    }

    /**
     * checkTransition
     */
    private fun checkTransition() {
        Log.d(TAG, "checkTransition")
        val transition = intent.getSerializableExtra(Transition.KEY) as? Transition
        transition?.let {
            Log.d(TAG, "transition ${it.type}")
            currentFragment?.transition(transition)
        }
    }

    private var lastHeight = 0

    /**
     * onStart
     */
    override fun onStart() {
        super.onStart()

        binding?.let {
            it.container.viewTreeObserver.addOnGlobalLayoutListener {

                // get Status bar top
                val rect = Rect()
                window.decorView.getWindowVisibleDisplayFrame(rect)

                val heightDiff = it.root.rootView.height - it.root.height - rect.top
                Log.d(TAG, "heightDiff = ${it.container.rootView.height} - ${it.root.height} - ${rect.top} = $heightDiff")

                val beforeHeight = lastHeight
                lastHeight = heightDiff

                // 初回測定の場合は何もしない
                if (beforeHeight == 0) return@addOnGlobalLayoutListener

                val fragment = currentFragment
                if (lastHeight > beforeHeight + 200) {
                    Log.d(TAG, "onShowKeyboard")
                    if (fragment is OnKeyboardListener)
                        fragment.onShowKeyboard()
                } else if (lastHeight < beforeHeight) {
                    Log.d(TAG, "onHideKeyboard")
                    if (fragment is OnKeyboardListener)
                        fragment.onHideKeyboard()
                }
            }

        }

        checkTransition()
    }

    /**
     * setKeyBoardValue
     *
     * @param word
     */
    fun setKeyBoardValue(word: String) {
        val actionView = menu?.findItem(R.id.search)?.actionView
        actionView?.let {
            val search = it as SearchView
            search.setQuery(word, false)
        }
    }

    /**
     * onCreateOptionsMenu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d(TAG, "onCreateOptionsMenu")
        menuInflater.inflate(R.menu.option_menu, menu)
        
        // Initially open
        val actionView = menu?.findItem(R.id.search)?.actionView
        actionView?.let {
            val search = it as SearchView

            search.isIconifiedByDefault = false
            search.queryHint = getString(R.string.searchbar_placeholder)
            search.isSubmitButtonEnabled = false
            search.maxWidth = Int.MAX_VALUE

            // focus
            search.setOnFocusChangeListener { view, b ->
                val fragment = currentFragment
                if (fragment is OnSearchViewListener)
                    fragment.focusChange(view, b)
            }

            // text listener
            search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(p0: String?): Boolean {
                    val fragment = currentFragment
                    if (fragment is OnSearchViewListener)
                        return fragment.onQueryTextChange(p0)
                    return false
                }

                override fun onQueryTextSubmit(p0: String?): Boolean {
                    val fragment = currentFragment
                    if (fragment is OnSearchViewListener)
                        return fragment.onQueryTextSubmit(p0)
                    return false
                }
            })

            // close
            search.setOnCloseListener {
                search.setQuery("", true)
                val fragment = currentFragment
                if (fragment is OnSearchViewListener)
                    fragment.clickCloseButton()
                return@setOnCloseListener true
            }
        }

        this.menu = menu
        return true
    }

    /**
     * onResume
     */
    override fun onResume() {
        super.onResume()

        Log.d(TAG, "onResume")
    }

    /**
     * getViewModelStore
     * 全画面で共有するShareViewModelを取得する
     *
     * @return ViewModelStore
     */
    override fun getViewModelStore(): ViewModelStore {
        return super<SharedViewModelStoreOwner>.getViewModelStore()
    }

    /**
     * setFragment
     * フラグメント切り替え時に呼ぶ関数
     *
     * @param fragment 切り替え先のフラグメント
     * @param isActiveCall
     */
    fun setFragment(fragment: Fragment, isActiveCall: Boolean = true) {
        Log.d(TAG, "set Fragment ${fragment.javaClass.name}")

        var setting = ActionBarMenuSetting()
        if (fragment is OnMenuSettingListener)
            setting = fragment.actionBarMenuSetting

        // Callback when Fragment is active
        if (isActiveCall)
            if (fragment is OnFragmentActiveListener) fragment.active()

        binding?.let {
            it.navView.visibility = if (setting.navVisible) View.VISIBLE else View.GONE
        }
        menu?.findItem(R.id.search)?.isVisible = setting.searchVisible
        menu?.findItem(R.id.update)?.isVisible = setting.updateVisible
        menu?.findItem(R.id.add)?.isVisible = setting.addVisible

        // topFragmentからupButtonフラグを呼び出す
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val top = topFragment
        if (top is TopFragmentListener)
            supportActionBar?.setDisplayHomeAsUpEnabled(top.isPlayHomeAsUpEnabled)

        Log.d(TAG, "backStackEntryCount = ${supportFragmentManager.backStackEntryCount}")
    }

    /**
     * refreshBatch
     */
    fun refreshBatch() {
        Log.d(TAG, "refreshBatch")

        UserApiClient().batchCount({ batches ->
            setBatch(0, batches.giron)
            setBatch(1, batches.notice)
            setBatch(2, batches.information + batches.advertisementCoin)

            BatchCountObjApi().setData(batches)
        }, {

        })
    }

    /**
     * setBatch
     *
     * @param index
     * @param value
     */
    private fun setBatch(index: Int, value: Int) {
        binding?.let{ binding ->
            val navView: BottomNavigationView = binding.navView
            navView.menu.getItem(index).itemId.let {
                navView.getOrCreateBadge(it).apply {
                    number = value
                    backgroundColor = getColor(R.color.colorPrimary)

                    if (number == 0) isVisible = false
                }
            }
        }
    }

    /**
     * onBackPressed
     * 戻るボタンが押された時
     */
    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed")

        // 戻るフラグ
        var backPress = true

        val fragment = currentFragment

        fragment?.let { Log.d(TAG, "currentFragment is ${fragment.javaClass.name}") }
        if (fragment is OnBackPressedListener)
            backPress = fragment.onBackPressed()

        if (!backPress) return

        val top = topFragment
        if (top is TopFragmentListener)
            if (!top.back())
                super.onBackPressed()
    }

    /**
     * onOptionsItemSelected
     *
     * @param item
     *
     * @return Boolean
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected ${item.itemId}")

        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.update -> {
                Log.d(TAG, "touch update menu")
                save()
            }
            R.id.add -> {
                Log.d(TAG, "touch add menu")
                add()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * save
     */
    private fun save() {
        val fragment = currentFragment
        if (fragment is OnUpdatePressedListener) fragment.update()
    }

    /**
     * add
     */
    private fun add() {
        val fragment = currentFragment
        if (fragment is OnAddPressedListener) fragment.add()
    }

    /**
     * 定義
     */
    companion object {
        private const val ADVERTISE_RESULT_CODE = 57438
        private const val TAG = "MainActivity"
    }
}
