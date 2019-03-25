package com.agilie.adssampleapp.app

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.adform.sdk.utils.AdApplicationService
import com.agilie.adssampleapp.objectbox.data.MyObjectBox
import com.apptracker.android.track.AppTracker
import com.inmobi.sdk.InMobiSdk
import io.objectbox.BoxStore
import org.json.JSONException
import org.json.JSONObject


class AdsSampleApplication : MultiDexApplication(), AdApplicationService.ServiceListener {

    companion object {
        var backPressListener: LeadboltActivityBackPressListener? = null
        lateinit var boxStore: BoxStore

        fun initInMobiIfNeeded(activity: Context) {
            InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG)
            val consentObject = JSONObject()
            try {
                consentObject.put(InMobiSdk.IM_GDPR_CONSENT_AVAILABLE, true)
                consentObject.put("gdpr", "0")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            InMobiSdk.init(activity, "5bc10b9c7f94426e85e7e15973224113", consentObject)
        }
    }

    private val APP_API_KEY = "is2byYEVjbXiFjVjaYIt6sM4aEIqMWZ3"
    private val APP_API_KEY_AGILIE_TEST = "hMQHu4B2Gd11ZM1jEE0ECu7D47S6Jsj6"

    private val adFormService: AdApplicationService by lazy { AdApplicationService.init() }

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(AdsSampleLifecycleCallbacks())

        AppTracker.startSession(this, APP_API_KEY, false)
//        AppLog.enableUserLog(true)
//        AppLog.enableLog(true)
//        AppLog.inspectLog(true)
        boxStore = MyObjectBox.builder().androidContext(this).build()
    }

    override fun getAdService(): AdApplicationService {
        return adFormService
    }

    interface LeadboltActivityBackPressListener {
        fun onAdBackPressed()
    }
}