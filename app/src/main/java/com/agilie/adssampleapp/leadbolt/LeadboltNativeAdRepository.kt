package com.agilie.adssampleapp.leadbolt

import com.apptracker.android.nativead.ATNativeAd
import io.reactivex.Single


class LeadboltNativeAdRepository {
    private val localDS = LeadboltNativeAdLocalDS
    private val remoteDS = LeadboltNativeAdRemoteDS()

    fun getNativeAds(refreshAd: Boolean = false): Single<List<ATNativeAd>> {

        //Should be some logic to find out should native ads be loaded

        return if (refreshAd) {
            remoteDS.fetchNativeAds()
                .flatMap { loadedAds ->
                    localDS.saveNativeAds(loadedAds)
                        .andThen(Single.just(loadedAds))
                }
        } else {
            localDS.getNativeAds()
                .flatMap { ads ->
                    if (ads.isEmpty()) {
                        return@flatMap remoteDS.fetchNativeAds()
                            .flatMap { loadedAds ->
                                localDS.saveNativeAds(loadedAds)
                                    .andThen(Single.just(loadedAds))
                            }
                    } else {
                        return@flatMap Single.just(ads)
                    }
                }
        }
    }
}