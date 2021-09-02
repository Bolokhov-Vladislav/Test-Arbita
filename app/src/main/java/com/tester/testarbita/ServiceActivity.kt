package com.tester.testarbita

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_service.*
import android.os.Build
import android.util.Log


class ServiceActivity : AppCompatActivity() {

    var manufacturer = Build.MANUFACTURER
    var model = Build.MODEL
    var version = Build.VERSION.SDK_INT
    var versionRelease = Build.VERSION.RELEASE
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        service_text.text = "GAID: $gAID \n " +
                "appsflyer_id: $apsId \n " +
                "campaing: $campaing \n " +
                "adgroup: $adgroup \n " +
                "adset: $adset \n " +
                "af_status: $afStatus \n " +
                "af_channel: $afChannel \n" +
                "remote_config: $urlF"

        device.text = "manufacturer: $manufacturer \n" +
                "model: $model \n" +
                "version: $version \n" +
                "versionRelease: $versionRelease"
    }
}