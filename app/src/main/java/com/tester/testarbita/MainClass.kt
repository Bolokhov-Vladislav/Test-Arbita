package com.tester.testarbita



import android.app.Application
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.onesignal.OneSignal


var adgroup:String? = null
var afChannel:String? = null
var adset:String? = null
var campaing:String? = null


var afStatus:String? = null
const val DevKey = "uBEyXJDUph9BYr4HCvVYMF"
const val ONESIGNAL_APP_ID = "e54fa523-6e0e-4819-b860-eb7bb922eb3d"


class MainClass : Application() {
    override fun onCreate() {
        super.onCreate()
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        val conversionDataListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                data?.let { cvData ->
                    cvData.map {
                        Log.i("APS", "conversion_attribute:  ${it.key} = ${it.value}")
                    }


                    if(cvData.getValue("af_status") == "Organic"){
                        afStatus = "Organic"
                    }else{
                        afStatus = "Non-Organic"

                        campaing = cvData.getValue("campaign").toString()
                        adgroup = cvData.getValue("adgroup").toString()
                        adset = cvData.getValue("adset").toString()
                        afChannel = cvData.getValue("af_channel").toString()
                    }



                }
            }
            override fun onConversionDataFail(error: String?) {}
            override fun onAppOpenAttribution(data: MutableMap<String, String>?) {}
            override fun onAttributionFailure(error: String?) {}
        }

        AppsFlyerLib.getInstance().init(DevKey, conversionDataListener, this)
        AppsFlyerLib.getInstance().start(this)

    }
}



