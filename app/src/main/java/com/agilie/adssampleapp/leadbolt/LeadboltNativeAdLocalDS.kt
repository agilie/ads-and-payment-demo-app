package com.agilie.adssampleapp.leadbolt

import com.agilie.adssampleapp.data.datasource.local.INativeAdLocalDataSource
import com.apptracker.android.nativead.ATNativeAd
import io.reactivex.Completable
import io.reactivex.Single

object LeadboltNativeAdLocalDS : INativeAdLocalDataSource {

    private var nativeAds: List<ATNativeAd> = emptyList()

    override fun getNativeAds(): Single<List<ATNativeAd>> {
        return Single.just(nativeAds)
    }

    override fun saveNativeAds(ads: List<ATNativeAd>): Completable {
        return Completable.complete()
            .doOnComplete { nativeAds = ads }
    }
}
