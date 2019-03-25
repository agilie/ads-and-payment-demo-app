package com.agilie.adssampleapp.presentation.view

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Animatable
import android.graphics.drawable.Animatable2
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.agilie.adssampleapp.EXTRA_IS_MOVES_LIST_SHOWN
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.domain.model.Cinema
import com.agilie.adssampleapp.presentation.CinemasMapViewModel
import com.agilie.adssampleapp.recyclerview.adapter.CinemasAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.concurrent.atomic.AtomicBoolean

abstract class CinemasMapActivity : AppCompatActivity() {

    protected val constraintSet = ConstraintSet()

    private val jumpingDuration = 750L
    private val jumpLong = 0.0002F

    private val mapCameraZoom = 16F

    protected val isAnimationStarted = AtomicBoolean(false)

    private lateinit var cinemasAdapter: CinemasAdapter
    private lateinit var cinemasViewModel: CinemasMapViewModel
    protected lateinit var map: GoogleMap

    protected val clRoot: ConstraintLayout  by lazy { getRootLayout() }

    protected val ivArrow: AppCompatImageView by lazy {
        getCinemasListArrow().apply {
            setOnClickListener {
                it.isSelected = !it.isSelected
                val animatable = (it as AppCompatImageView).drawable as? Animatable
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    animatable?.let { anim ->
                        when (anim) {
                            is Animatable2Compat -> setEndListener(anim)
                            is Animatable2 -> setEndListener(anim)
                        }
                    }
                } else {
                    if (animatable is Animatable2Compat) {
                        setEndListener(animatable)
                    }
                }
                animatable?.start()

                if (it.isSelected) hideCinemasList() else displayCinemasList()
            }
        }
    }

    private val markerIcon: Bitmap by lazy {
        initMarkerIcon()
    }

    private val jumpingAnimation by lazy {
        ValueAnimator.ofFloat(0f, 2f).apply {
            duration = jumpingDuration
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
        }
    }

    private val endAnimationListener: () -> Unit = {
        if (ivArrow.isSelected) {
            ivArrow.setImageResource(R.drawable.arrow_left_to_right)
        } else {
            ivArrow.setImageResource(R.drawable.arrow_right_to_left)
        }
    }

    private val listItemClickListener: (cinema: Cinema) -> Unit = {
        cinemasViewModel.onCinemaSelected(it)
    }

    private val cinemaObserver: Observer<Cinema> = Observer { cinema ->
        map.run {
            clear()
            addMarker(
                MarkerOptions()
                    .position(cinema.coordinates)
                    .title(cinema.name)
                    .icon(BitmapDescriptorFactory.fromBitmap(markerIcon))
            ).apply {
                tag = cinema.id
                showInfoWindow()
            }
            animateCamera(CameraUpdateFactory.newLatLngZoom(cinema.coordinates, mapCameraZoom))
        }
    }

    protected val mapMarkerClickListener = GoogleMap.OnMarkerClickListener { marker ->
        displayFullScreenAds(marker.tag as Int)
        starAnimateMarker(marker)
        marker.showInfoWindow()

        map.run {
            animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, mapCameraZoom))
            setOnMarkerClickListener(null)
        }

        return@OnMarkerClickListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getRootLayoutId())

        cinemasViewModel = ViewModelProviders.of(this).get(CinemasMapViewModel::class.java)
            .also { vm ->
                vm.observeCinemasList(this, Observer { cinemas ->
                    cinemasAdapter.setCinemas(cinemas)
                    if (cinemas.isEmpty()) {
                        ivArrow.visibility = View.GONE
                    } else {
                        ivArrow.visibility = View.VISIBLE
                    }
                    if (savedInstanceState != null) {
                        if (!savedInstanceState.getBoolean(EXTRA_IS_MOVES_LIST_SHOWN)) {
                            ivArrow.isSelected = true
                            hideCinemasList(false)
                            endAnimationListener()
                        }
                    }
                })
            }

        getMapFragment()?.getMapAsync {
            map = it.apply {
                setOnMarkerClickListener(mapMarkerClickListener)
                setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        this@CinemasMapActivity,
                        R.raw.google_map_style
                    )
                )
            }
            cinemasViewModel.let { vm ->
                vm.onMapReady()
                vm.observeSelectedCinema(this, cinemaObserver)
            }
        }

        cinemasAdapter = CinemasAdapter(clickListener = listItemClickListener)

        getCinemasListView().run {
            setHasFixedSize(true)
            adapter = cinemasAdapter
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(EXTRA_IS_MOVES_LIST_SHOWN, !ivArrow.isSelected)
        super.onSaveInstanceState(outState)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun initMarkerIcon(): Bitmap {
        val iconDrawable = resources.getDrawable(R.drawable.ic_marker_cinema)
        val iconBitmap = Bitmap.createBitmap(
            iconDrawable.intrinsicWidth,
            iconDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(iconBitmap)
        iconDrawable.setBounds(0, 0, canvas.width, canvas.height)
        iconDrawable.draw(canvas)
        return iconBitmap
    }

    private fun setEndListener(animatableCompat: Animatable2Compat? = null) {
        animatableCompat?.let {
            it.clearAnimationCallbacks()
            it.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    super.onAnimationEnd(drawable)
                    endAnimationListener.invoke()
                }
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setEndListener(animatable: Animatable2? = null) {
        animatable?.let {
            it.clearAnimationCallbacks()
            it.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    super.onAnimationEnd(drawable)
                    endAnimationListener.invoke()
                }
            })
        }
    }

    private fun displayCinemasList(animate: Boolean = true) {
        constraintSet.run {
            clone(clRoot)
            clear(R.id.rvCinemaList, ConstraintSet.START)
            connect(
                R.id.rvCinemaList, ConstraintSet.END,
                R.id.clRoot, ConstraintSet.END,
                resources.getDimensionPixelSize(R.dimen.cinemas_list_horizontal_margin)
            )
            applyTo(clRoot)
        }

        if (animate) {
            val transition = ChangeBounds().apply {
                duration = resources.getInteger(R.integer.arrow_left_right_duration).toLong()
            }
            TransitionManager.beginDelayedTransition(clRoot, transition)
        }
    }

    private fun hideCinemasList(animate: Boolean = true) {
        constraintSet.run {
            clone(clRoot)
            clear(R.id.rvCinemaList, ConstraintSet.END)
            connect(
                R.id.rvCinemaList, ConstraintSet.START,
                R.id.clRoot, ConstraintSet.END
            )
            applyTo(clRoot)
        }

        if (animate) {
            val transition = ChangeBounds().apply {
                duration = resources.getInteger(R.integer.arrow_left_right_duration).toLong()
            }
            TransitionManager.beginDelayedTransition(clRoot, transition)
        }
    }

    private fun createUpdateAnimationListener(marker: Marker): ValueAnimator.AnimatorUpdateListener {
        val pos = marker.position

        return ValueAnimator.AnimatorUpdateListener {
            if (isAnimationStarted.get()) {
                val currentValue = it.animatedValue as Float
                when {
                    currentValue <= 1 -> marker.position =
                        LatLng(pos.latitude + currentValue * jumpLong, pos.longitude)
                    else -> marker.position =
                        LatLng(pos.latitude + (2 - currentValue) * jumpLong, pos.longitude)
                }
            } else {
                it.cancel()
                marker.position = pos
            }
        }
    }

    private fun starAnimateMarker(marker: Marker) {
        isAnimationStarted.set(true)

        jumpingAnimation.run {
            cancel()
            removeAllUpdateListeners()
            addUpdateListener(createUpdateAnimationListener(marker))
            start()
        }
    }

    protected fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    @LayoutRes
    abstract fun getRootLayoutId(): Int

    abstract fun getRootLayout(): ConstraintLayout

    abstract fun getCinemasListArrow(): AppCompatImageView

    abstract fun getCinemasListView(): RecyclerView

    abstract fun getMapFragment(): SupportMapFragment?

    abstract fun displayFullScreenAds(cinemaId: Int)
}