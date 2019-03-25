package com.agilie.adssampleapp.adform

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.*
import com.adform.sdk.interfaces.AdListener
import com.adform.sdk.interfaces.AdStateListener
import com.adform.sdk.pub.AdOverlay
import com.adform.sdk.pub.views.AdInline
import com.adform.sdk.pub.views.AdRecyclerViewItemBuilder
import com.adform.sdk.utils.AdApplicationService
import com.adform.sdk.utils.AdSize
import com.adform.sdk.utils.SmartAdSize
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.advertising.*

class AdFormAdProvider : AdProvider {

    private val bigBannerMasterTag = 4672767
    private val customGridMasterTag = 4016318
    private val defaultBannerMasterTag = 4022668
    private val fullScreenMasterTag = 4935199

    private val adAdapterManager by lazy { AdFormAdAdapterManager() }

    private var bannerBig: AdInline? = null
    private var bannerDefault: AdInline? = null
    private var fullScreenAd: AdOverlay? = null

    /**
     * The default height of view is 50dp. The default height is set by AdForm SDK. So we need to
     * use a custom height to display an ad in a view like a carousel. It is the big banner for our case
     */
    private val bannerBigHeightDp = 250

    override fun init(context: Context) {
        val app = context.applicationContext
        if (app is AdApplicationService.ServiceListener) {
            app.adService
            initFullScreenAd(context)
        } else {
            throw RuntimeException("The application should implement the ${AdApplicationService.ServiceListener::class.java.simpleName}")
        }
    }

    override fun inflateDefaultBanner(parent: ViewGroup, lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
        if (bannerDefault == null) {
            bannerDefault = initBanner(parent, defaultBannerMasterTag, SmartAdSize())
            parent.apply {
                removeAllViews()
                addView(bannerDefault)
            }
        }
    }

    override fun loadAdForDefaultBanner(lifecycleOwner: LifecycleOwner, observer: Observer<AdLoadingEvent>) {
        val liveData = MutableLiveData<AdLoadingEvent>().apply {
            observe(lifecycleOwner, observer)
        }
        loadAd(bannerDefault, liveData)
    }

    override fun inflateBigBanner(parent: ViewGroup, lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)

        if (bannerBig == null) {
            bannerBig = initBanner(parent, bigBannerMasterTag, SmartAdSize(1, bannerBigHeightDp))
            parent.apply {
                removeAllViews()
                addView(bannerBig)
            }
        }
    }

    override fun loadAdForBigBanner(lifecycleOwner: LifecycleOwner, observer: Observer<AdLoadingEvent>) {
        val liveData = MutableLiveData<AdLoadingEvent>().apply {
            observe(lifecycleOwner, observer)
        }
        loadAd(bannerBig, liveData)
    }

    private fun loadAd(banner: AdInline?, liveData: MutableLiveData<AdLoadingEvent>) {
        return if (banner != null) {
            banner.loadAd()
            setBannerListeners(banner, liveData)
        } else {
            liveData.postValue(
                AdLoadingEvent(
                    AdLoadingEventType.FAILED,
                    "The banner should be inflated before start loading ad!"
                )
            )
        }
    }

    private fun setBannerListeners(adView: AdInline, liveData: MutableLiveData<AdLoadingEvent>) {
        adView.run {
            setListener(object : AdListener {
                override fun onAdLoadFail(banner: AdInline, errorMessage: String) {
                    liveData.postValue(AdLoadingEvent(AdLoadingEventType.FAILED, errorMessage))
                }

                override fun onAdLoadSuccess(banner: AdInline) {
                    liveData.postValue(AdLoadingEvent(AdLoadingEventType.SUCCESS))
                }
            })
            setStateListener(object : AdStateListener {
                override fun onAdOpen(banner: AdInline?) {
                }

                override fun onAdVisibilityChange(banner: AdInline, isVisible: Boolean) {
                    liveData.postValue(AdLoadingEvent(AdLoadingEventType.SUCCESS))
                }

                override fun onAdClose(banner: AdInline) {
                }
            })
        }
    }

    override fun displayFullScreenAd(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        observer: Observer<FullScreenAdEvent>
    ) {

        lifecycleOwner.lifecycle.addObserver(this)

        if (fullScreenAd == null) {
            initFullScreenAd(context)
        }

        val resultLiveData = MutableLiveData<FullScreenAdEvent>().apply {
            observe(lifecycleOwner, observer)
        }

        addFullScreenListeners(resultLiveData)
        fullScreenAd?.showAd()
    }

    override fun createGridViewHolder(parent: ViewGroup): AdViewHolder {
        val view = AdRecyclerViewItemBuilder.init(parent.context, customGridMasterTag, SmartAdSize(), true)
        return object : AdViewHolder(view) {

            override fun bind() {
                //The AdInlineLW takes all responsibility to display an ad data
            }

            override fun onRecycled() {
                //The AdInlineLW takes all responsibility to handle an ad data
            }
        }
    }

    override fun getAdAdapterManager(): AdRecyclerView.AdAdapterManager {
        return adAdapterManager
    }

    override fun getAdLayoutManager(): AdRecyclerView.AdLayoutManager {
        return AdFormAdLayoutManager(adAdapterManager)
    }

    private fun initBanner(parent: ViewGroup, masterTag: Int, size: AdSize): AdInline {
        val inflater = LayoutInflater.from(parent.context)
        val adView = inflater.inflate(R.layout.ad_default_banner_ad_form, parent, false) as AdInline

        return adView.apply {
            masterTagId = masterTag
            adSize = size
            setDebugMode(true)
        }
    }

    private fun initFullScreenAd(context: Context) {
        fullScreenAd = AdOverlay.createInstance(context).apply {
            setMasterTagId(fullScreenMasterTag)
            setDebugMode(true)
        }
    }

    private fun addFullScreenListeners(liveData: MutableLiveData<FullScreenAdEvent>) {

        fullScreenAd?.run {
            setStateListener(object : AdOverlay.OverlayStateListener {
                override fun onAdShown() {
                    liveData.postValue(FullScreenAdEvent.DISPLAYED)
                }

                override fun onAdClose() {
                    liveData.postValue(FullScreenAdEvent.CLOSED)
                }
            })

            setListener(object : AdOverlay.OverlayLoaderListener {
                override fun onLoadSuccess() {
                    liveData.postValue(FullScreenAdEvent.LOADED)
                }

                override fun onLoadError(errorMessage: String?) {
                    liveData.postValue(FullScreenAdEvent.FAILED_TO_LOAD)
                }

                override fun onShowError(errorMessage: String?) {
                    liveData.postValue(FullScreenAdEvent.FAILED_TO_DISPLAY)
                }

            })

            setAdClickListener(object : AdOverlay.AdOverlayClickListener {
                override fun onAdLeftApplication(p0: AdOverlay?) {
                    liveData.postValue(FullScreenAdEvent.HAS_LEFT_APP)
                }

                override fun onAdClick(p0: AdOverlay?) {
                    liveData.postValue(FullScreenAdEvent.CLICKED)
                }

            })
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onOwnerResume() {
        bannerBig?.onResume()
        bannerDefault?.onResume()
        fullScreenAd?.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onOwnerPause() {
        bannerBig?.onPause()
        bannerDefault?.onPause()
        fullScreenAd?.onPause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onOwnerDestroy() {
        bannerBig?.destroy()
        bannerBig = null

        bannerDefault?.destroy()
        bannerDefault = null

        fullScreenAd?.destroy()
        fullScreenAd = null
    }

    fun onSaveInstanceState(outState: Bundle) {
        fullScreenAd?.onSaveInstanceState(outState)
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        fullScreenAd?.onRestoreInstanceState(savedInstanceState)
    }
}
