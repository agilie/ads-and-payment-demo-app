package com.agilie.adssampleapp.advertising

import android.content.Context
import android.view.ViewGroup
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

interface AdProvider : LifecycleObserver {
    fun init(context: Context)

    fun inflateDefaultBanner(parent: ViewGroup, lifecycleOwner: LifecycleOwner)

    fun loadAdForDefaultBanner(lifecycleOwner: LifecycleOwner, observer: Observer<AdLoadingEvent>)

    fun inflateBigBanner(parent: ViewGroup, lifecycleOwner: LifecycleOwner)

    fun loadAdForBigBanner(lifecycleOwner: LifecycleOwner, observer: Observer<AdLoadingEvent>)

    fun displayFullScreenAd(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        observer: Observer<FullScreenAdEvent>
    )

    fun createGridViewHolder(parent: ViewGroup): AdViewHolder

    fun getAdAdapterManager(): AdRecyclerView.AdAdapterManager

    fun getAdLayoutManager(): AdRecyclerView.AdLayoutManager
}