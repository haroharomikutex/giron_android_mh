package com.giron.android.view.login

import android.os.Bundle
import android.app.AlertDialog
import android.content.Intent
import android.view.View
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.giron.android.view.main.MainActivity
import com.giron.android.R
import com.giron.android.databinding.ActivityLoginBinding
import com.giron.android.extension.setOnSafeClickListener
import com.giron.android.util.*
import com.giron.android.model.dao.KeyValueObj
import com.giron.android.model.dao.OnetimeToken
import com.giron.android.model.dao.Transition
import com.giron.android.model.net.realm.KeyValueObjApi
import com.giron.android.model.net.realm.UserObjApi
import com.giron.android.model.net.ApiClient
import com.giron.android.model.net.UserApiClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.install.model.AppUpdateType
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson

/**
 * LoginActivity
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private var transition: Transition? = null
    private var request: OnetimeToken? = null
    private var retryFlag = false

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // google sign in
        binding.googleSign.setOnClickListener {
            signInGoogle()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        // 通知からの遷移かチェック
        transition = Transition.fromIntent(intent)
        request = OnetimeToken.fromIntent(intent)
        checkVersion()
    }

    /**
     * onActivityResult
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_INTENT_CODE) {
            // Googleログイン
            signInWithGoogle(data)
        }  else if(requestCode == EULA_REQUEST_CODE) {
            // 同意済みか再度チェック
            checkEula()
        } else if(requestCode == UPDATE_REQUEST_CODE) {
            // 強制アップデート
            if (resultCode == RESULT_OK) {
                // リクエストOK
                Log.d(TAG, "Update OK!")
                checkEula()
            } else {
                Log.d(TAG,"Update flow failed! Result code: $resultCode")

                // もう一度処理へ
                forceUpdate()
            }
        }
    }

    /**
     * onStart
     */
    override fun onStart() {
        super.onStart()

        binding.main.setOnSafeClickListener {
            if(retryFlag) {
                retryFlag = false
                checkVersion()
            }
        }
    }

    /**
     * onStop
     */
    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    /**
     * onDestroy
     */
    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    /**
     * googleからのcallback処理
     *
     * @param data
     */
    private fun signInWithGoogle(data: Intent?) {
        val signInAccount = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            // アカウント取得
            val account = signInAccount.getResult(ApiException::class.java)

            account?.also {
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                firebaseSignWithCredential(credential)
            } ?: run {
                failedSignUp()
                Log.w(TAG, "Google sign in failed")
            }
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e.fillInStackTrace())
            failedSignUp()
        }
    }

    /**
     * firebaseログイン
     */
    private fun firebaseSignWithCredential(credential: AuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (!task.isSuccessful)  {
                Log.e(TAG, "signInWithCredential:error ${task.exception.toString()}")
                failedSignUp(task.exception.toString())
                return@addOnCompleteListener
            }
            Log.d(TAG, "signInWithCredential:success")

            val user = auth.currentUser ?: run {
                Log.w(TAG, "Get current user failed")
                failedSignUp()
                return@addOnCompleteListener
            }

            // token取得チャレンジ
            user.getIdToken(true).addOnCompleteListener(OnCompleteListener<GetTokenResult> { result ->
                if (!result.isSuccessful)  {
                    Log.e(TAG, "getIdToken:error")
                    failedSignUp()
                    return@OnCompleteListener
                }

                // token取得
                val idToken = result.result?.token
                signUp(idToken)

                Log.d(TAG, "idToken: $idToken")
            })

        }
    }

    /**
     * sign_inチェック
     */
    private fun signIn() {
        Log.d(TAG, "sign in")

        // メッセージ表示
        binding.loginInfoText.text = getString(R.string.now_login)

        UserApiClient().signIn(
            {userEntity ->
                Log.d(TAG, "result: " + Gson().toJson(userEntity))

                setDeviceToken()

                // realmに取得したデータを保存
                UserObjApi().setData(userEntity)

                // Main Activityに遷移
                transMainActivity()
            },
            {throwable ->
                Log.w(TAG, "result: "+ Gson().toJson(throwable))
                visibleSignButton()
            }
        )
    }

    /**
     * setDeviceToken
     */
    private fun setDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            task.result?.let { token ->
                // Log and toast
                Log.d(TAG, "Success Get DeviceToken: $token")

                UserApiClient().setDeviceToken(token,
                    {resultEntity ->
                        Log.d(TAG, "result: " + Gson().toJson(resultEntity))
                    },
                    {throwable ->
                        Log.w(TAG, "result: "+ Gson().toJson(throwable))
                    }
                )
            }
        }
    }

    /**
     * サインインボタン表示
     */
    private fun visibleSignButton() {
        binding.loginInfoText.text = ""
        binding.googleSign.visibility = View.VISIBLE
    }

    /**
     * サインアップ
     */
    private fun signUp(idToken: String?) {
        // メッセージ表示
        binding.loginInfoText.text = getString(R.string.now_login)

        UserApiClient().signUp(
            idToken,
            { userEntity ->
                Log.d(TAG, "result: " + Gson().toJson(userEntity))

                setDeviceToken()

                // realmに取得したデータを保存
                UserObjApi().setData(userEntity)

                // 取得したトークンをセットする
                KeyValueObjApi().setValue(KeyValueObj.Key.GIRON_TOKEN, userEntity.token)

                // Main Activityに遷移
                transMainActivity()
            },
            {throwable ->
                Log.w(TAG, "result: "+ Gson().toJson(throwable))
                failedSignUp()
            }
        )
    }

    /**
     * failedSignUp
     * サインアップ失敗時の処理
     *
     * @param message
     */
    private fun failedSignUp(message: String = "") {
        // uiの変更
        binding.loginInfoText.text = ""
        binding.googleSign.isEnabled = true
        binding.googleSign.alpha = 1.0F

        var mes = message
        if (mes.isEmpty()) mes = getString(R.string.failed_sign_up)

        AlertDialog.Builder(this)
                .setMessage(mes)
                .show()
    }

    /**
     * googleサインイン
     */
    private fun signInGoogle() {
        // メッセージ変更
        binding.loginInfoText.text = getString(R.string.get_certification)

        // ボタンをクリック不可、半透明に
        binding.googleSign.isEnabled = false
        binding.googleSign.alpha = 0.5F

        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, GOOGLE_SIGN_INTENT_CODE)
    }

    /**
     * バージョンチェック
     */
    private fun checkVersion() {
        // メッセージ変更
        Log.d(TAG, "checking version")
        binding.loginInfoText.text = getString(R.string.checking_app_version)

        ApiClient().checkVersion({ result ->
            if (result.result) {
                Log.d(TAG, "version OK")
                checkEula()
            } else {
                Log.d(TAG, "version NG")
                forceUpdate()
            }
        },{
            showError()
        })
    }

    /**
     * エラー表示
     */
    private fun showError() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.error_ocuccered))
            .setPositiveButton("OK") { _, _ ->
                checkVersion()
            }.show()

        retryFlag = true
    }

    /**
     * checkEula
     * 規約確認
     */
    private fun checkEula() {
        // 同意済みバージョン取得
        Log.d(TAG, "checking Eula")
        val eulaVersion = KeyValueObjApi().getValue(KeyValueObj.Key.EULA_VERSION)

        // eulaチェック済みか
        eulaVersion?.also { version ->
            Log.d(TAG, "need Eula version: $EULA_VERSION")
            Log.d(TAG, "checked Eula version: $version")
            // 最新バージョンに同意済みか
            if (version == EULA_VERSION)
                checkToken()
            else
                viewEula()

        } ?: run {
            viewEula()
        }

    }

    /**
     * viewEula
     */
    private fun viewEula() {
        Log.d(TAG, "view Eula")
        val intent = Intent(application, EulaActivity::class.java)
        startActivityForResult(intent, EULA_REQUEST_CODE)
        overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top)
    }

    /**
     * token確認
     */
    private fun checkToken() {
        if (KeyValueObjApi().hasObj<KeyValueObj>(KeyValueObj.Key.GIRON_TOKEN)) {
            signIn()
        } else {
            visibleSignButton()
        }
    }

    /**
     * 強制アップデートチェック
     */
    private fun forceUpdate() {
        Log.d(TAG, "force_update")
        binding.loginInfoText.text = ""

        val appUpdateManager = AppUpdateManagerFactory.create(this)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // 強制アップデート
                appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, UPDATE_REQUEST_CODE)
            } else {
                // 次の処理へ
                checkEula()
            }
        }
    }

    /**
     * transMainActivity
     */
    private fun transMainActivity() {
        val intent = Intent(application, MainActivity::class.java)
        intent.putExtra(Transition.KEY, transition)
        intent.putExtra(OnetimeToken.KEY, request)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top)
    }

    /**
     * static
     */
    companion object {
        private const val TAG = "LoginActivity"
        private const val GOOGLE_SIGN_INTENT_CODE = 1000
        private const val UPDATE_REQUEST_CODE = 2000
        private const val EULA_REQUEST_CODE = 3000
    }
}
