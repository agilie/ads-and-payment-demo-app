package com.agilie.adssampleapp.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.domain.model.Cinema
import com.agilie.adssampleapp.recyclerview.viewholder.CinemaHolder

class CinemasAdapter(
        private var cinemas: List<Cinema> = emptyList(),
        private inline val clickListener: ((cinema: Cinema) -> Unit)? = null
) : RecyclerView.Adapter<CinemaHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): CinemaHolder {
        return CinemaHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_cinema_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return cinemas.size
    }

    override fun onBindViewHolder(holder: CinemaHolder, position: Int) {
        holder.onBind(cinemas[position])
        holder.itemView.setOnClickListener {
            val pos = holder.adapterPosition
            clickListener?.invoke(cinemas[pos])
        }
    }

    override fun onViewRecycled(holder: CinemaHolder) {
        holder.itemView.setOnClickListener(null)
    }

    fun setCinemas(cinemas: List<Cinema>?) {
        this.cinemas = cinemas ?: emptyList()
        notifyDataSetChanged()
    }

}