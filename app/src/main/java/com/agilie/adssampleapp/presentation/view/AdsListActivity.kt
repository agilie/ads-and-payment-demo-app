package com.agilie.adssampleapp.presentation.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.agilie.adssampleapp.EXTRA_AD_PROVIDE_TYPE
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.advertising.AdProvidersType
import com.agilie.adssampleapp.app.AdsSampleApplication
import com.agilie.adssampleapp.data.datasource.remote.MoviesGenresRemoteDS
import com.agilie.adssampleapp.data.repository.MoviesGenresRepository
import com.agilie.adssampleapp.objectbox.datasource.MoviesGenresLocalDS
import com.agilie.adssampleapp.recyclerview.adapter.AdProvidersAdapter
import io.reactivex.disposables.Disposable

class AdsListActivity : AppCompatActivity() {
    var moviesGenresDisposable: Disposable? = null

    val itemClickListener: ((id: Int, type: AdProvidersType) -> Unit)? =
        { id: Int, type: AdProvidersType ->
            startActivity(
                Intent(this, AdProviderCinemasMapActivity::class.java)
                    .apply { putExtra(EXTRA_AD_PROVIDE_TYPE, type.ordinal) }
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ads_list)
        setSupportActionBar(findViewById(R.id.toolbar))
        title = getString(R.string.app_name)

        findViewById<RecyclerView>(R.id.rvAdsList).apply {
            setHasFixedSize(true)
            adapter = AdProvidersAdapter(
                AdProvidersType.values().toList(),
                itemClickListener
            )
        }
        loadMoviesGenres()
    }

    private fun loadMoviesGenres() {
        moviesGenresDisposable?.dispose()
        moviesGenresDisposable = MoviesGenresRepository(
            MoviesGenresLocalDS(AdsSampleApplication.boxStore),
            MoviesGenresRemoteDS()
        )
            .getMovies()
            .onErrorReturnItem(emptyList())
            .subscribe()
    }
}