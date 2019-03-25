package com.agilie.adssampleapp.recyclerview.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.agilie.adssampleapp.BuildConfig
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.domain.model.Movie
import com.agilie.adssampleapp.glide.GlideApp

class MovieHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
    private val ivImage: AppCompatImageView = rootView.findViewById(R.id.ivPoster)
    private val tvPosterName: AppCompatTextView = rootView.findViewById(R.id.tvPosterName)
    private val tvPosterDate: AppCompatTextView = rootView.findViewById(R.id.tvPosterDate)
    val viewPosterFilter: View = rootView.findViewById(R.id.viewPosterFilter)

    fun onBind(movie: Movie) {
        tvPosterName.text = movie.title
        tvPosterDate.text = movie.getReleaseYear()
        GlideApp.with(rootView)
            .load(BuildConfig.BASE_URL_POSTER + movie.poster)
            .fitCenter()
            .into(ivImage)
    }
}