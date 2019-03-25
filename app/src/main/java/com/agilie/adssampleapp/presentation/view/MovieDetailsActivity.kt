package com.agilie.adssampleapp.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.agilie.adssampleapp.EXTRA_AD_PROVIDE_TYPE
import com.agilie.adssampleapp.EXTRA_CINEMA_ID
import com.agilie.adssampleapp.EXTRA_MOVIE_ID
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.advertising.*
import com.agilie.adssampleapp.databinding.ActivityMovieDetailsBinding
import com.agilie.adssampleapp.domain.model.MovieSession
import com.agilie.adssampleapp.presentation.*
import com.agilie.adssampleapp.presentation.view.StripeCardDialogFragment.Companion.ARGS_KEY_SESSION
import com.stripe.android.model.Token

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var adProvider: AdProvider
    private lateinit var viewModel: StripeCardViewModel
    private lateinit var scheduleViewModel: ScheduleViewModel
    private var movieId: Long = 0L

    private var stripeCardDialog: DialogFragment? = null
    private var successPurchaseDialog: DialogFragment? = null

    private val cinemaId by lazy {
        intent.getIntExtra(EXTRA_CINEMA_ID, 1)
    }

    private val observer: Observer<AdLoadingEvent> = Observer { adEvent ->
        if (adEvent.event == AdLoadingEventType.SUCCESS) {
            binding.llAdContainer.run {
                postDelayed(
                    {
                        visibility = View.VISIBLE
                        layoutParams = layoutParams.apply {
                            height = ViewGroup.LayoutParams.WRAP_CONTENT
                        }
                        requestLayout()
                    },
                    500
                )
            }
        } else {
            Log.d("LOAD_ERROR", adEvent.errorMessage)
        }
    }

    private val clickListener = View.OnClickListener { view: View ->
        MovieSessionsDialogFragment.getInstance(cinemaId, movieId)
            .show(supportFragmentManager, "MovieSessionsDialogFragment")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details)
        setSupportActionBar(findViewById(R.id.toolbar))
        title = ""

        movieId = intent.getLongExtra(EXTRA_MOVIE_ID, 0L)
        ViewModelProviders.of(this, MovieDetailsViewModelFactory(movieId))
            .get(MovieDetailsViewModel::class.java)
            .let { vm ->
                vm.observeMovie(this, Observer { binding.movie = it })
                vm.observeMovieCast(this, Observer { binding.tvCast.text = it })
                vm.observeMovieDetails(this, Observer { binding.details = it })
            }

        ViewModelProviders.of(this, ScheduleViewModelFactory(cinemaId, movieId))
            .get(ScheduleViewModel::class.java)
            .let { it.observeSessionSelection(this, Observer { showStripeDialog(it) }) }

        val adProvideType = intent.getIntExtra(EXTRA_AD_PROVIDE_TYPE, AdProvidersType.LEADBOLT.ordinal)

        adProvider = AdProvidersFactory.getProvider(AdProvidersType.values()[adProvideType]).apply {
            inflateBigBanner(binding.llAdContainer, this@MovieDetailsActivity)
            loadAdForBigBanner(this@MovieDetailsActivity, observer)
        }

        binding.btnBuyTicket.setOnClickListener(clickListener)
        viewModel = ViewModelProviders.of(this).get(StripeCardViewModel::class.java)
        viewModel.observeToken(this, Observer { onTokenReceived(it) })
        viewModel.observeTokenError(this, Observer { onTokenReceivingFailed(it) })
    }

    private fun onTokenReceived(token: Token?) {
        stripeCardDialog?.dismiss()
        if (successPurchaseDialog == null) {
            successPurchaseDialog = SuccessPurchaseDialogFragment()
        }
        successPurchaseDialog?.show(supportFragmentManager, "SuccessPurchaseDialogFragment")
    }

    private fun onTokenReceivingFailed(errorMessage: String?) {
        if (!errorMessage.isNullOrBlank()) {
            Toast.makeText(this, errorMessage, LENGTH_LONG).show()
        }
    }

    private fun showStripeDialog(session: MovieSession) {
        val dialog = supportFragmentManager?.findFragmentByTag("MovieSessionsDialogFragment")
        (dialog as? DialogFragment)?.dismiss()

        if (stripeCardDialog == null) {
            stripeCardDialog = StripeCardDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARGS_KEY_SESSION, session)
                }
            }
        }
        stripeCardDialog?.show(supportFragmentManager, "StripeCardDialogFragment")

    }
}