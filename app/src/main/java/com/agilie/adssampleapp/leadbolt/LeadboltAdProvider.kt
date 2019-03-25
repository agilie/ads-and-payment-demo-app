package com.agilie.adssampleapp.leadbolt

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.*
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.advertising.*
import com.agilie.adssampleapp.glide.GlideApp
import com.apptracker.android.listener.AppModuleListener
import com.apptracker.android.nativead.ATNativeAd
import com.apptracker.android.nativead.template.ATNativeAdView
import com.apptracker.android.track.AppTracker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class LeadboltAdProvider : AdProvider {

    private val APP_API_KEY = "is2byYEVjbXiFjVjaYIt6sM4aEIqMWZ3"
    private val APP_API_KEY_AGILIE_TEST = "hMQHu4B2Gd11ZM1jEE0ECu7D47S6Jsj6"

    private val adsNonRewardedType = "inapp"
    private val adsRewardedType = "reward"
    private val adsType = adsNonRewardedType

    private val nativeAdRepository = LeadboltNativeAdRepository()
    private val adList = arrayListOf<ATNativeAd>()
    private val holdersList = arrayListOf<LeadboltViewHolder>()

    private var bannerDisposable: Disposable? = null
    private var bannerDefaultParent: ViewGroup? = null
    private var bannerBigParent: ViewGroup? = null

    private var isAdLoadStarted = false

    override fun init(context: Context) {
        AppTracker.startSession(context.applicationContext, APP_API_KEY, false)
//        AppLog.enableUserLog(true)
//        AppLog.enableLog(true)
//        AppLog.inspectLog(true)
    }

    override fun inflateDefaultBanner(parent: ViewGroup, lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
        bannerDefaultParent = parent
    }

    override fun loadAdForDefaultBanner(lifecycleOwner: LifecycleOwner, observer: Observer<AdLoadingEvent>) {
        loadBanner(ATNativeAdView.Type.HEIGHT_100, bannerDefaultParent ?: return, false)
            .observe(lifecycleOwner, observer)
    }

    override fun inflateBigBanner(parent: ViewGroup, lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
        bannerBigParent = parent
    }

    override fun loadAdForBigBanner(lifecycleOwner: LifecycleOwner, observer: Observer<AdLoadingEvent>) {
        loadBanner(ATNativeAdView.Type.HEIGHT_300, bannerBigParent ?: return, true)
            .observe(lifecycleOwner, observer)
    }

    override fun displayFullScreenAd(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        observer: Observer<FullScreenAdEvent>
    ) {

        val eventsLiveData = MutableLiveData<FullScreenAdEvent>()

        AppTracker.setModuleListener(object : AppModuleListener {
            override fun onModuleCached(s: String) {
                AppTracker.loadModule(context, adsType)
            }

            override fun onModuleClosed(s: String, b: Boolean) {
                eventsLiveData.postValue(FullScreenAdEvent.CLOSED)
            }

            override fun onModuleLoaded(s: String) {
                eventsLiveData.postValue(FullScreenAdEvent.LOADED)
            }

            override fun onModuleClicked(s: String) {
                eventsLiveData.postValue(FullScreenAdEvent.CLICKED)
            }

            override fun onModuleFailed(type: String, cause: String, b: Boolean) {
                eventsLiveData.postValue(FullScreenAdEvent.FAILED_TO_LOAD)
            }
        })

        if (AppTracker.isAdReady(adsType)) {
            AppTracker.loadModule(context, adsType)
        } else {
            AppTracker.loadModuleToCache(context, adsType)
        }

        eventsLiveData.observe(lifecycleOwner, observer)
    }

    override fun createGridViewHolder(parent: ViewGroup): AdViewHolder {

        if (!isAdLoadStarted) {
            isAdLoadStarted = true
            val dis = nativeAdRepository.getNativeAds(true)
                .subscribe(
                    {
                        adList.addAll(it)
                        bindAdToHolders()
                    },
                    { error -> error.printStackTrace() }
                )
        }

        val inflater = LayoutInflater.from(parent.context)
        return LeadboltViewHolder(
            inflater.inflate(
                R.layout.cell_leadbolt_movie_ad_item,
                parent,
                false
            )
        )
    }

    override fun getAdAdapterManager(): AdRecyclerView.AdAdapterManager {
        return LeadboltAdAdapterManager()
    }

    override fun getAdLayoutManager(): AdRecyclerView.AdLayoutManager {
        return LeadboltAdLayoutManager()
    }

    private fun loadBanner(
        bannerType: ATNativeAdView.Type,
        parent: ViewGroup,
        refresh: Boolean
    ): LiveData<AdLoadingEvent> {

        val liveData = MutableLiveData<AdLoadingEvent>()

        bannerDisposable?.dispose()
        bannerDisposable = nativeAdRepository.getNativeAds(refresh)
            .filter { !it.isEmpty() }
            .observeOn(AndroidSchedulers.mainThread())
            .map { ATNativeAdView.nativeAdViewWithAd(parent.context, it[0], bannerType) }
            .doOnSuccess {
                parent.apply {
                    visibility = View.VISIBLE
                    removeAllViews()
                    addView(it)
                }
            }
            .map { AdLoadingEvent(AdLoadingEventType.SUCCESS) }
            .onErrorReturn { AdLoadingEvent(AdLoadingEventType.FAILED, it.message ?: "") }
            .subscribe(
                { liveData.postValue(it) },
                { liveData.postValue(AdLoadingEvent(AdLoadingEventType.FAILED, it.message ?: "")) }
            )

        return liveData
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onOwnerDestroyed() {
        bannerDisposable?.dispose()
        bannerDefaultParent = null
        bannerBigParent = null
    }

    private fun bindAdToHolders() {
        if (!adList.isEmpty()) {
            val adSize = adList.size
            for (i in holdersList.indices) {
                val adIndex = i % adSize
                holdersList[i].bind(adList[adIndex])
            }
        }
    }

    inner class LeadboltViewHolder(view: View) : AdViewHolder(view) {

        val ivAdImage: AppCompatImageView = view.findViewById(R.id.ivAdImage)
        val tvAdText: AppCompatTextView = view.findViewById(R.id.tvAdText)

        override fun bind() {
            if (this !in holdersList) {
                holdersList.add(this)
            }

            if (!adList.isEmpty()) {
                val adIndex = holdersList.indexOf(this) % adList.size
                bind(adList[adIndex])
            }
        }

        override fun onRecycled() {
            holdersList.remove(this)
        }

        fun bind(ad: ATNativeAd) {
            ad.let {
                itemView.post {
                    tvAdText.text = it.title
                    it.registerViewForInteraction(itemView)
                    it.htmlResourceUrls
                        .filter { triple -> triple.second == "file" }
                        .map { triple -> triple.first.toString() }
                        .forEach { imageUrl ->
                            GlideApp.with(itemView)
                                .load(imageUrl)
                                .fitCenter()
                                .into(ivAdImage)
                        }
                }
            }
        }
    }
}