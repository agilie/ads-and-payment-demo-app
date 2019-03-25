package com.agilie.adssampleapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.agilie.adssampleapp.app.AdsSampleApplication
import com.agilie.adssampleapp.data.repository.MoviesRepository
import com.agilie.adssampleapp.domain.model.Movie
import com.agilie.adssampleapp.objectbox.datasource.MoviesLocalDS
import io.reactivex.disposables.Disposable

class MoviesListViewModel : ViewModel() {

    private var moviesDisposable: Disposable? = null
    private val movies = MutableLiveData<List<Movie>?>()

    init {
        moviesDisposable = MoviesRepository(
                MoviesLocalDS(
                        AdsSampleApplication.boxStore
                )
        ).getMovies()
            .subscribe(
                { moviesList -> movies.postValue(moviesList) },
                { error -> error.printStackTrace() }
            )
    }

    override fun onCleared() {
        moviesDisposable?.dispose()
    }

    fun observeMovies(owner: LifecycleOwner, observer: Observer<List<Movie>?>) {
        movies.observe(owner, observer)
    }
}