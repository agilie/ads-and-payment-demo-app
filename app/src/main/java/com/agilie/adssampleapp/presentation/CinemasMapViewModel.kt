package com.agilie.adssampleapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.agilie.adssampleapp.data.repository.CinemaRepository
import com.agilie.adssampleapp.domain.model.Cinema
import io.reactivex.disposables.Disposable
import java.util.concurrent.atomic.AtomicBoolean

class CinemasMapViewModel : ViewModel() {

    private var cinemasList: List<Cinema>? = null
    private var cinemasDisposable: Disposable? = null

    private val cinemaLiveData = MutableLiveData<Cinema>()
    private val cinemasLiveData = MutableLiveData<List<Cinema>>()
    private val isMapReady = AtomicBoolean(false)

    init {
        cinemasDisposable = CinemaRepository().fetchCinemaList()
            .subscribe(
                {
                    cinemasList = it
                    if (isMapReady.get()) {
                        cinemasLiveData.postValue(cinemasList)
                    }
                },
                { error -> error.printStackTrace() }
            )
    }

    override fun onCleared() {
        super.onCleared()
        cinemasDisposable?.dispose()
    }

    fun observeSelectedCinema(owner: LifecycleOwner, observer: Observer<Cinema>) {
        cinemaLiveData.observe(owner, observer)
    }

    fun observeCinemasList(owner: LifecycleOwner, observer: Observer<List<Cinema>>) {
        cinemasLiveData.observe(owner, observer)
    }

    fun onMapReady() {
        isMapReady.set(true)
        if (cinemasList != null) {
            cinemasLiveData.postValue(cinemasList)
        }
    }

    fun onCinemaSelected(cinema: Cinema) {
        cinemaLiveData.postValue(cinema)
    }
}