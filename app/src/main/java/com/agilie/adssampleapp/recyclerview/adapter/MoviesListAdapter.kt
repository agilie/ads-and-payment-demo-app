package com.agilie.adssampleapp.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.advertising.AdProvider
import com.agilie.adssampleapp.advertising.AdViewHolder
import com.agilie.adssampleapp.domain.model.Movie
import com.agilie.adssampleapp.recyclerview.viewholder.MovieHolder

class MoviesListAdapter(
    movies: List<Movie>? = null,
    private val adProvider: AdProvider,
    private inline val clickListener: ((position: Int, movie: Movie?) -> Unit)?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_AD = 0
        const val TYPE_MOVIE = 1
    }

    private val adAdapterManager = adProvider.getAdAdapterManager()

    var movies: List<Movie>? = movies
        set(value) {
            field = value ?: ArrayList()
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, itemViewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (itemViewType) {
            TYPE_AD -> adProvider.createGridViewHolder(parent)
            else -> MovieHolder(inflater.inflate(R.layout.cell_movie_item, parent, false))
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (adAdapterManager.isAdItem(holder.adapterPosition)) {
            (holder as? AdViewHolder)?.onRecycled()
        }
    }

    override fun getItemCount(): Int {
        return adAdapterManager.getItemsCount(movies?.size ?: 0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieHolder -> {
                holder.run {
                    onBind(getMovie(position) ?: return)
                    viewPosterFilter.setOnClickListener {
                        val pos = holder.adapterPosition
                        clickListener?.invoke(pos, getMovie(pos))
                    }
                }
            }
            is AdViewHolder -> holder.bind()
            else -> return
        }
    }

    private fun getMovie(position: Int): Movie? {
        return movies?.get(adAdapterManager.getItemPosition(position))
    }

    override fun getItemViewType(position: Int): Int {
        return if (adAdapterManager.isAdItem(position)) {
            TYPE_AD
        } else {
            TYPE_MOVIE
        }
    }
}