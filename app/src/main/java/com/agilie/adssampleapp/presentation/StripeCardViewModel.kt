package com.agilie.adssampleapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import io.reactivex.Single
import io.reactivex.disposables.Disposable

class StripeCardViewModel(app: Application) : AndroidViewModel(app) {

    private var tokenDisposable: Disposable? = null

    private val tokenLiveData = MutableLiveData<Token?>()
    private val tokenErrorLiveData = MutableLiveData<String?>()

    private val stripe by lazy {
        Stripe(getApplication(), "pk_test_TYooMQauvdEDq54NiTphI7jx")
    }

    private val tokenCallback: TokenCallback by lazy {
        object : TokenCallback {
            override fun onSuccess(token: Token?) {
                tokenLiveData.postValue(token)
            }

            override fun onError(error: Exception?) {
                onTokenError(error)
            }
        }
    }

    fun buyViaStripeCard(card: Card) {
        tokenDisposable = Single
            .fromCallable { stripe.createToken(card, tokenCallback) }
            .doOnError { onTokenError(it) }
            .onErrorResumeNext { Single.just(Unit) }
            .subscribe()
    }

    fun observeToken(owner: LifecycleOwner, observer: Observer<Token?>) {
        tokenLiveData.observe(owner, observer)
    }

    fun observeTokenError(owner: LifecycleOwner, observer: Observer<String?>) {
        tokenErrorLiveData.observe(owner, observer)
    }

    private fun onTokenError(error: Throwable?) {
        if (error != null) {
            error.printStackTrace()
            tokenErrorLiveData.postValue(error.message)
        }
    }
}