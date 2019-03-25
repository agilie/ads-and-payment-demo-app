package com.agilie.adssampleapp.data.datasource.remote

import com.apptracker.android.nativead.ATNativeAd
import io.reactivex.Single

interface INativeAdRemoteDataSource {
    fun fetchNativeAds(): Single<List<ATNativeAd>>
}