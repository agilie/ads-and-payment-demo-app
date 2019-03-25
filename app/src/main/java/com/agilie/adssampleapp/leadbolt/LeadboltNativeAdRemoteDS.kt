package com.agilie.adssampleapp.leadbolt

import com.agilie.adssampleapp.data.datasource.remote.INativeAdRemoteDataSource
import com.apptracker.android.nativead.ATNativeAd
import com.apptracker.android.track.AppTracker
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class LeadboltNativeAdRemoteDS : INativeAdRemoteDataSource {

    private val subject = PublishSubject.create<List<ATNativeAd>>()

    init {
        AppTracker.setNativeListener(
            LeadboltATNativeListenerImp(
                onAdsLoaded = { subject.onNext(it?.ads.orEmpty()) },
                logPrefix = "LeadboltNativeAdRemoteDS -> "
            )
        )
    }

    override fun fetchNativeAds(): Single<List<ATNativeAd>> {
        return Single.just(AppTracker.loadNativeAdsWithCaching())
            .flatMap { subject.firstOrError() }
    }
}