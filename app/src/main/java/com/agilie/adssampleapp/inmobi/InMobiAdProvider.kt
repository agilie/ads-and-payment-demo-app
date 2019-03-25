package com.agilie.adssampleapp.inmobi

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.advertising.*
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiBanner
import com.inmobi.ads.InMobiInterstitial
import com.inmobi.ads.InMobiNative
import com.inmobi.ads.listeners.BannerAdEventListener
import com.inmobi.ads.listeners.InterstitialAdEventListener
import com.inmobi.ads.listeners.NativeAdEventListener
import com.inmobi.sdk.InMobiSdk
import org.json.JSONException
import org.json.JSONObject
import px

class InMobiAdProvider : AdProvider {

    companion object {
        const val INMOBI_BANNER_WIDTH_DP = 468
        const val INMOBI_BANNER_HEIGHT_DP = 60

        const val INMOBI_BANNER_DEFAULT_PLACEMENT_ID = 1548388170803
        const val INMOBI_BANNER_BIG_PLACEMENT_ID = 1545563941755
        const val INMOBI_INTERSTITIAL_PLACEMENT_ID = 1546051370459
        const val INMOBI_CUSTOM_GRID_PLACEMENT_ID = 1549288720888

        const val INMOBI_BANNER_REFRESH_INTERVAL = 60 // 60 seconds
    }

    private lateinit var bigBannerListener: NativeAdEventListener

    private var bigBanner: InMobiNative? = null
    private var bigBannerParent: ViewGroup? = null
    private var defaultBanner: InMobiBanner? = null
    private var defaultBannerListener: BannerAdEventListener? = null
    private var fullScreenAd: InMobiInterstitial? = null
    private var fullScreenListener: InterstitialAdEventListener? = null

    private val listAdMap = hashMapOf<Int, InMobiNative>()
    private val listAdListenerMap = hashMapOf<Int, NativeAdEventListener>()
    private val viewHolderMap = hashMapOf<Int, InMobiViewHolder>()

    override fun init(context: Context) {
        InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG)
        val consentObject = JSONObject()
        try {
            consentObject.put(InMobiSdk.IM_GDPR_CONSENT_AVAILABLE, true)
            consentObject.put("gdpr", "0")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        InMobiSdk.init(context, "5bc10b9c7f94426e85e7e15973224113", consentObject)
    }

    override fun inflateDefaultBanner(parent: ViewGroup, lifecycleOwner: LifecycleOwner) {
        defaultBanner = InMobiBanner(parent.context, INMOBI_BANNER_DEFAULT_PLACEMENT_ID).apply {
            setRefreshInterval(INMOBI_BANNER_REFRESH_INTERVAL)
            setEnableAutoRefresh(true)

            layoutParams = ViewGroup.LayoutParams(INMOBI_BANNER_WIDTH_DP.px, INMOBI_BANNER_HEIGHT_DP.px)
        }
        parent.run {
            removeAllViews()
            addView(defaultBanner)
        }
    }

    override fun loadAdForDefaultBanner(lifecycleOwner: LifecycleOwner, observer: Observer<AdLoadingEvent>) {

        val liveData = MutableLiveData<AdLoadingEvent>().apply {
            observe(lifecycleOwner, observer)
        }

        defaultBannerListener = object : BannerAdEventListener() {
            override fun onAdLoadSucceeded(banner: InMobiBanner?) {
                liveData.postValue(AdLoadingEvent(AdLoadingEventType.SUCCESS))
            }

            override fun onAdLoadFailed(banner: InMobiBanner, status: InMobiAdRequestStatus) {
                liveData.postValue(AdLoadingEvent(AdLoadingEventType.FAILED, errorMessageFromStatus(status)))
            }

            override fun onRequestPayloadCreationFailed(status: InMobiAdRequestStatus) {
                liveData.postValue(AdLoadingEvent(AdLoadingEventType.FAILED, errorMessageFromStatus(status)))
            }
        }

        defaultBanner?.run {
            setListener(defaultBannerListener)
            load()
        }
    }

    override fun inflateBigBanner(parent: ViewGroup, lifecycleOwner: LifecycleOwner) {
        bigBannerParent = parent
    }

    override fun loadAdForBigBanner(lifecycleOwner: LifecycleOwner, observer: Observer<AdLoadingEvent>) {

        val liveData = MutableLiveData<AdLoadingEvent>().apply {
            observe(lifecycleOwner, observer)
        }

        bigBannerListener = object : NativeAdEventListener() {
            override fun onAdLoadSucceeded(ad: InMobiNative) {
                bigBannerParent?.run {
                    post {
                        removeAllViews()
                        addView(bigBanner?.getPrimaryViewOfWidth(context, this, this, width))
                        liveData.postValue(AdLoadingEvent(AdLoadingEventType.SUCCESS))
                    }
                }
            }

            override fun onAdLoadFailed(ad: InMobiNative, status: InMobiAdRequestStatus) {
                liveData.postValue(AdLoadingEvent(AdLoadingEventType.FAILED, errorMessageFromStatus(status)))
            }
        }

        bigBanner = InMobiNative(bigBannerParent?.context, INMOBI_BANNER_BIG_PLACEMENT_ID, bigBannerListener)
            .apply { load(bigBannerParent?.context) }
    }

    override fun displayFullScreenAd(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        observer: Observer<FullScreenAdEvent>
    ) {

        val liveData = MutableLiveData<FullScreenAdEvent>().apply {
            observe(lifecycleOwner, observer)
        }

        fullScreenListener = object : InterstitialAdEventListener() {
            override fun onAdLoadSucceeded(ad: InMobiInterstitial?) {
                liveData.postValue(FullScreenAdEvent.LOADED)
                ad?.show()
            }

            override fun onAdLoadFailed(p0: InMobiInterstitial?, p1: InMobiAdRequestStatus?) {
                liveData.postValue(FullScreenAdEvent.FAILED_TO_LOAD)
            }

            override fun onAdDisplayed(ad: InMobiInterstitial?) {
                liveData.postValue(FullScreenAdEvent.DISPLAYED)
            }

            override fun onAdDismissed(p0: InMobiInterstitial?) {
                liveData.postValue(FullScreenAdEvent.CLOSED)
            }

            override fun onAdDisplayFailed(p0: InMobiInterstitial?) {
                liveData.postValue(FullScreenAdEvent.FAILED_TO_DISPLAY)
            }

            override fun onUserLeftApplication(p0: InMobiInterstitial?) {
                liveData.postValue(FullScreenAdEvent.HAS_LEFT_APP)
            }

            override fun onAdClicked(p0: InMobiInterstitial?, p1: MutableMap<Any, Any>?) {
                liveData.postValue(FullScreenAdEvent.CLICKED)
            }
        }
        fullScreenAd = InMobiInterstitial(context, INMOBI_INTERSTITIAL_PLACEMENT_ID, fullScreenListener)
            .apply { load() }
    }

    override fun createGridViewHolder(parent: ViewGroup): AdViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val holder = InMobiViewHolder(
            inflater.inflate(
                R.layout.cell_inmobi_movie_ad_item,
                parent,
                false
            )
        )
        Log.d("TEST_ADAPTER", "create --> $holder")
        return holder
    }

    override fun getAdAdapterManager(): AdRecyclerView.AdAdapterManager {
        return InMobiAdAdapterManager()
    }

    override fun getAdLayoutManager(): AdRecyclerView.AdLayoutManager {
        return InMobiAdLayoutManager()
    }

    private fun errorMessageFromStatus(status: InMobiAdRequestStatus): String {
        return "Error code - ${status.statusCode}; ${status.message}"
    }

    inner class InMobiViewHolder(view: View) : AdViewHolder(view) {

        private val adContainer = itemView.findViewById<LinearLayout>(R.id.adContainer)
        private val tvErrorCode = itemView.findViewById<AppCompatTextView>(R.id.tvErrorCode)
        private val tvErrorMessage = itemView.findViewById<AppCompatTextView>(R.id.tvErrorMessage)

        override fun bind() {
            adContainer.visibility = View.VISIBLE
            tvErrorCode.visibility = View.GONE
            tvErrorMessage.visibility = View.GONE

            viewHolderMap[adapterPosition] = this
            val ad = listAdMap[adapterPosition]
            if (ad != null && ad.isReady) {
                bind(ad)
            } else {
                val adListener = AdListener(adapterPosition)
                listAdListenerMap[adapterPosition] = adListener
                listAdMap[adapterPosition] =
                    InMobiNative(itemView.context, INMOBI_CUSTOM_GRID_PLACEMENT_ID, adListener)
                        .apply { load() }
            }
        }

        override fun onRecycled() {
            var position: Int? = null
            for ((pos, holder) in viewHolderMap) {
                position = pos
                if (holder === this) {
                    break
                }
            }
            if (position != null) {
                viewHolderMap.remove(position)
            }
        }

        fun bind(ad: InMobiNative) {
            ad.run {
                itemView.post {
                    val adView = getPrimaryViewOfWidth(itemView.context, itemView, adContainer, 110.px)
                    if (adView != null) adContainer.addView(adView)
                }
            }
        }

        fun bindError(status: InMobiAdRequestStatus) {
            adContainer.visibility = View.GONE
            tvErrorCode.visibility = View.VISIBLE
            tvErrorMessage.visibility = View.VISIBLE

            tvErrorCode.text = status.statusCode.toString()
            tvErrorMessage.text = status.message
        }
    }

    inner class AdListener(val position: Int) : NativeAdEventListener() {
        override fun onAdLoadSucceeded(ad: InMobiNative) {
            listAdMap[position] = ad
            viewHolderMap[position]?.bind(ad)
        }

        override fun onAdLoadFailed(ad: InMobiNative, status: InMobiAdRequestStatus) {
            listAdMap.remove(position)
            viewHolderMap[position]?.bindError(status)
        }
    }
}