package com.agilie.adssampleapp.recyclerview.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.domain.model.Cinema
import com.agilie.adssampleapp.glide.GlideApp

class CinemaHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
    val ivCinemaLogo: AppCompatImageView = rootView.findViewById(R.id.ivCinemaLogo)
    val tvCinemaName: AppCompatTextView = rootView.findViewById(R.id.tvCinemaName)

    fun onBind(cinema: Cinema?) {
        if (cinema != null) {
            tvCinemaName.text = cinema.name
            GlideApp.with(itemView)
                .load(cinema.logoUrl)
                .fitCenter()
                .into(ivCinemaLogo)
        }
    }
}