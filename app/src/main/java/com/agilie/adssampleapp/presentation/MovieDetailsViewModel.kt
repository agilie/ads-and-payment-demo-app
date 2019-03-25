package com.agilie.adssampleapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.agilie.adssampleapp.app.AdsSampleApplication
import com.agilie.adssampleapp.data.datasource.remote.MovieCharactersRemoteDS
import com.agilie.adssampleapp.data.datasource.remote.MovieDetailsRemoteDS
import com.agilie.adssampleapp.data.repository.MoviesCharactersRepository
import com.agilie.adssampleapp.data.repository.MoviesDetailsRepository
import com.agilie.adssampleapp.data.repository.MoviesRepository
import com.agilie.adssampleapp.domain.model.Movie
import com.agilie.adssampleapp.domain.model.MovieDetails
import com.agilie.adssampleapp.objectbox.datasource.MovieCharactersLocalDS
import com.agilie.adssampleapp.objectbox.datasource.MovieDetailsLocalDS
import com.agilie.adssampleapp.objectbox.datasource.MoviesLocalDS
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

class MovieDetailsViewModel(private val movieId: Long) : ViewModel() {

    private var movieDisposable: Disposable? = null
    private var movieCastDisposable: Disposable? = null
    private var movieDetailsDisposable: Disposable? = null

    private val movie = MutableLiveData<Movie>()
    private val movieCast = MutableLiveData<String?>()
    private val movieDetails = MutableLiveData<MovieDetails>()

    private val moviesRepository = MoviesRepository(MoviesLocalDS(AdsSampleApplication.boxStore))
    private val movieDetailsRepository = MoviesDetailsRepository(
            MovieDetailsLocalDS(AdsSampleApplication.boxStore),
            MovieDetailsRemoteDS()
    )
    private val movieCharactersRepository = MoviesCharactersRepository(
            MovieCharactersLocalDS(AdsSampleApplication.boxStore),
            MovieCharactersRemoteDS()
    )

    private val errorConsumer: Consumer<Throwable> by lazy {
        return@lazy Consumer<Throwable> { error -> error.printStackTrace() }
    }

    init {
        fetchMovieDetails()
    }

    override fun onCleared() {
        super.onCleared()
        movieDisposable?.dispose()
        movieCastDisposable?.dispose()
        movieDetailsDisposable?.dispose()
    }

    fun observeMovie(owner: LifecycleOwner, observer: Observer<Movie>) {
        movie.observe(owner, observer)
    }

    fun observeMovieDetails(owner: LifecycleOwner, observer: Observer<MovieDetails>) {
        movieDetails.observe(owner, observer)
    }

    fun observeMovieCast(owner: LifecycleOwner, observer: Observer<String?>) {
        movieCast.observe(owner, observer)
    }

    private fun fetchMovieDetails() {
        movieDisposable = moviesRepository.getMovies()
            .flattenAsObservable { it }
            .filter { it.id == movieId }
            .subscribe(
                Consumer { movie.postValue(it) },
                errorConsumer
            )
        movieDetailsDisposable = movieDetailsRepository.getMovieDetails(movieId)
            .subscribe(
                Consumer { movieDetails.postValue(it) },
                errorConsumer
            )
        movieCastDisposable = movieCharactersRepository.getMoviesCast(movieId)
            .map { characters -> characters.joinToString { it.name } }
            .subscribe(
                Consumer { movieCast.postValue(it) },
                errorConsumer
            )
    }
}