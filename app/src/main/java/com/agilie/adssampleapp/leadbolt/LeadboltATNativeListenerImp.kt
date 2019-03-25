package com.agilie.adssampleapp.leadbolt

import android.util.Log
import com.apptracker.android.nativead.ATNativeAd
import com.apptracker.android.nativead.ATNativeAdCollection
import com.apptracker.android.nativead.ATNativeListener

class LeadboltATNativeListenerImp(
    private inline val onAdClicked: ((ad: ATNativeAd?) -> Unit)? = null,
    private inline val onAdDisplayed: ((ad: ATNativeAd?) -> Unit)? = null,
    private inline val onAdsLoaded: ((ad: ATNativeAdCollection?) -> Unit)? = null,
    private inline val onAdsFailed: ((ad: String?) -> Unit)? = null,
    private val logTag: String = "AppTracker_app",
    private val logPrefix: String = ""
) : ATNativeListener {

    override fun onAdClicked(ad: ATNativeAd?) {
        Log.d(logTag, "${logPrefix}onAdClicked")
        onAdClicked?.invoke(ad)
    }

    override fun onAdDisplayed(ad: ATNativeAd?) {
        Log.d(logTag, "${logPrefix}onAdDisplayed")
        onAdDisplayed?.invoke(ad)
    }

    override fun onAdsLoaded(adsCollection: ATNativeAdCollection?) {
        Log.d(logTag, "${logPrefix}onAdsLoaded: size ${adsCollection?.ads?.size}")
        onAdsLoaded?.invoke(adsCollection)
    }

    override fun onAdsFailed(message: String?) {
        Log.d(logTag, "${logPrefix}onAdsFailed: $message")
        onAdsFailed?.invoke(message)
    }
}