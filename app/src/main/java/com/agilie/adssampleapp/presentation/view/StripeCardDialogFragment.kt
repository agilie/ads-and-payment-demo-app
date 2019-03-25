package com.agilie.adssampleapp.presentation.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.binding.BindingDateFormatter
import com.agilie.adssampleapp.databinding.DialogFragmentStripeCardBinding
import com.agilie.adssampleapp.domain.model.MovieSession
import com.agilie.adssampleapp.presentation.MovieDetailsViewModel
import com.agilie.adssampleapp.presentation.StripeCardViewModel
import java.util.concurrent.atomic.AtomicBoolean

class StripeCardDialogFragment : DialogFragment() {

    companion object {
        const val ARGS_KEY_SESSION = "StripeCardDialogFragment.ARGS_KEY_SESSION"
    }

    private var isActionBlocked = AtomicBoolean(false)
    private val userActionHandler = Handler(Looper.getMainLooper())
    private val userActionDelay = 150L

    private val btnClickListener = View.OnClickListener {
        if (!isActionBlocked.get()) {
            isActionBlocked.set(true)
            val action: () -> Unit = when (it.id) {
                R.id.btnCancel -> {
                    { hide() }
                }
                R.id.btnBuy -> {
                    { getCard() }
                }
                else -> {
                    {}
                }
            }
            userActionHandler.postDelayed(getRunnable(action), userActionDelay)
        }
    }

    private var viewModel: StripeCardViewModel? = null
    private lateinit var movieViewModel: MovieDetailsViewModel
    private lateinit var binding: DialogFragmentStripeCardBinding
    private var session: MovieSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity ?: return).get(StripeCardViewModel::class.java)
        movieViewModel = ViewModelProviders.of(activity ?: return).get(MovieDetailsViewModel::class.java)
        session = arguments?.getSerializable(ARGS_KEY_SESSION) as? MovieSession
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_stripe_card, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            btnBuy.setOnClickListener(btnClickListener)
            btnCancel.setOnClickListener(btnClickListener)
            session = this@StripeCardDialogFragment.session
            dateFormatter = BindingDateFormatter()
        }

        movieViewModel.observeMovie(this, Observer { binding.movie = it })
        viewModel?.observeToken(this, Observer { binding.cmwCard.clear() })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userActionHandler.removeCallbacksAndMessages(null)
    }

    private fun getRunnable(action: () -> Unit): Runnable {
        return Runnable {
            action()
            isActionBlocked.set(false)
        }
    }

    private fun hide() {
        dismiss()
    }

    private fun hideKeyboard() {
        val focusedView = view?.findFocus()
        if (focusedView != null) {
            val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(focusedView.windowToken, 0)
            }
        }
    }

    private fun getCard() {
        val card = binding.cmwCard.card
        if (card != null) {
            viewModel?.buyViaStripeCard(card)
            binding.flLoading.visibility = View.VISIBLE
            hideKeyboard()
        }
    }
}