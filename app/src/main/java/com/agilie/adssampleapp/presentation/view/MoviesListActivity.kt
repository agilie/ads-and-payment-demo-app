package com.agilie.adssampleapp.presentation.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.agilie.adssampleapp.EXTRA_AD_PROVIDE_TYPE
import com.agilie.adssampleapp.EXTRA_CINEMA_ID
import com.agilie.adssampleapp.EXTRA_MOVIE_ID
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.advertising.AdProvidersFactory
import com.agilie.adssampleapp.advertising.AdProvidersType
import com.agilie.adssampleapp.domain.model.Movie
import com.agilie.adssampleapp.presentation.MoviesListViewModel
import com.agilie.adssampleapp.recyclerview.adapter.MoviesListAdapter

class MoviesListActivity : AppCompatActivity() {

    private var moviesAdapter: MoviesListAdapter? = null
    private val adProviderType by lazy {
        intent.getIntExtra(EXTRA_AD_PROVIDE_TYPE, AdProvidersType.LEADBOLT.ordinal)
    }
    private val cinemaId by lazy {
        intent.getIntExtra(EXTRA_CINEMA_ID, 1)
    }

    private val adProvider by lazy {
        AdProvidersFactory.getProvider(AdProvidersType.values()[adProviderType])
    }

    private val itemClickListener = { position: Int, movie: Movie? ->
        if (movie != null) {
            val intent = Intent(this, MovieDetailsActivity::class.java)
                .apply {
                    putExtra(EXTRA_MOVIE_ID, movie.id)
                    putExtra(EXTRA_CINEMA_ID, cinemaId)
                    putExtra(EXTRA_AD_PROVIDE_TYPE, adProviderType)
                }
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_movies_list)
        setSupportActionBar(findViewById(R.id.toolbar))
        title = getString(R.string.coming_soon)

        moviesAdapter = MoviesListAdapter(
            clickListener = itemClickListener,
            adProvider = adProvider
        )

        ViewModelProviders.of(this).get(MoviesListViewModel::class.java).let { vm ->
            vm.observeMovies(this, Observer { moviesAdapter?.movies = it })
        }

        findViewById<RecyclerView>(R.id.rvMoviesList).run {
            setHasFixedSize(true)
            adapter = moviesAdapter
            layoutManager = adProvider.getAdLayoutManager().getLayoutManager(this@MoviesListActivity)
        }
    }
}