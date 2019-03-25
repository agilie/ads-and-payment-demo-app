package com.agilie.adssampleapp.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.agilie.adssampleapp.EXTRA_AD_PROVIDE_TYPE
import com.agilie.adssampleapp.EXTRA_CINEMA_ID
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.adform.AdFormAdProvider
import com.agilie.adssampleapp.advertising.*
import com.agilie.adssampleapp.app.AdsSampleApplication
import com.google.android.gms.maps.SupportMapFragment
import java.util.concurrent.atomic.AtomicBoolean

class AdProviderCinemasMapActivity : CinemasMapActivity(),
    AdsSampleApplication.LeadboltActivityBackPressListener {

    private var isAdBackPressed = AtomicBoolean(false)

    private val llAdContainer: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.llAdContainer)
    }

    private val adProviderType by lazy {
        intent.getIntExtra(EXTRA_AD_PROVIDE_TYPE, AdProvidersType.LEADBOLT.ordinal)
    }

    private lateinit var adProvider: AdProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adProvider = AdProvidersFactory.getProvider(AdProvidersType.values()[adProviderType])
            .also {
                it.init(this)
                it.inflateDefaultBanner(llAdContainer, this)
                it.loadAdForDefaultBanner(this, Observer { adEvent ->
                    if (adEvent.event == AdLoadingEventType.SUCCESS) {
                        displayNativeAdTemplate()
                    } else {
                        Log.d("LOAD_ERROR", adEvent.errorMessage)
                    }
                })
            }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        if (adProvider is AdFormAdProvider) {
            (adProvider as? AdFormAdProvider)?.onSaveInstanceState(outState ?: return)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        if (adProvider is AdFormAdProvider) {
            (adProvider as? AdFormAdProvider)?.onRestoreInstanceState(savedInstanceState ?: return)
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun getRootLayoutId(): Int {
        return R.layout.activity_cinemas_map
    }

    override fun getRootLayout(): ConstraintLayout {
        return findViewById(R.id.clRoot)
    }

    override fun getCinemasListArrow(): AppCompatImageView {
        return findViewById(R.id.ivArrow)
    }

    override fun getCinemasListView(): RecyclerView {
        return findViewById(R.id.rvCinemaList)
    }

    override fun getMapFragment(): SupportMapFragment? {
        return supportFragmentManager.findFragmentById(R.id.frMap) as? SupportMapFragment
    }

    override fun onAdBackPressed() {
        isAdBackPressed.set(true)
    }

    private fun displayNativeAdTemplate() {

        llAdContainer.postDelayed(
            {
                llAdContainer.apply {
                    layoutParams = llAdContainer.layoutParams.apply {
                        height = WRAP_CONTENT
                    }
                    requestLayout()
                }
                mapToAd()
            },
            500
        )
    }

    private fun mapToAd() {
        constraintSet.run {
            clone(clRoot)
            connect(R.id.frMap, ConstraintSet.BOTTOM, llAdContainer.id, ConstraintSet.TOP)
            applyTo(clRoot)
        }
    }

    override fun displayFullScreenAds(cinemaId: Int) {
        AdsSampleApplication.backPressListener = this

        adProvider.displayFullScreenAd(this, this, Observer {
            when (it) {
                FullScreenAdEvent.LOADED -> isAnimationStarted.set(false)
                FullScreenAdEvent.CLOSED -> onFullScreenAdClosed(cinemaId)
                FullScreenAdEvent.FAILED_TO_DISPLAY -> {
                    //TODO: Add implementation of the errors handling
                }
                else -> {
                }
            }
        })
    }

    private fun onFullScreenAdClosed(cinemaId: Int) {
        map.setOnMarkerClickListener(mapMarkerClickListener)
        if (!isAdBackPressed.getAndSet(false)) {
            startActivity(
                Intent(this, MoviesListActivity::class.java)
                    .apply {
                        putExtra(EXTRA_AD_PROVIDE_TYPE, adProviderType)
                        putExtra(EXTRA_CINEMA_ID, cinemaId)
                    }
            )
        }
    }
}