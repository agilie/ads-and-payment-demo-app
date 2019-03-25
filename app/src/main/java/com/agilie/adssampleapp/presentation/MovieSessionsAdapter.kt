package com.agilie.adssampleapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.domain.model.MovieSession

class MovieSessionsAdapter(
    private inline val itemClickListener: (pos: Int, session: MovieSession) -> Unit? = { _, _ -> }
) : RecyclerView.Adapter<MovieSessionViewHolder>() {

    var sessions = emptyList<MovieSession>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSessionViewHolder {
        return MovieSessionViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_cinema_session, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieSessionViewHolder, position: Int) {
        val session = sessions[position]
        holder.bind(session)
        holder.flSessionContainer.setOnClickListener { itemClickListener(position, session) }
    }

    override fun getItemCount(): Int {
        return sessions.size
    }
}