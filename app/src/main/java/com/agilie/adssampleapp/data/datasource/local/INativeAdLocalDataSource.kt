package com.agilie.adssampleapp.data.datasource.local

import com.apptracker.android.nativead.ATNativeAd
import io.reactivex.Completable
import io.reactivex.Single

interface INativeAdLocalDataSource {
    fun getNativeAds(): Single<List<ATNativeAd>>

    fun saveNativeAds(ads: List<ATNativeAd>): Completable
}