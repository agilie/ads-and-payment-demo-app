package com.agilie.adssampleapp.presentation

import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.domain.model.MovieSession
import com.agilie.adssampleapp.domain.model.MovieSessionAvailable
import java.text.SimpleDateFormat
import java.util.*

class MovieSessionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val flSessionContainer = view.findViewById<FrameLayout>(R.id.flSessionContainer)
    val tvSessionTime = view.findViewById<AppCompatTextView>(R.id.tvSessionTime)
    val ivTicketAvailability = view.findViewById<AppCompatImageView>(R.id.ivTicketAvailability)

    fun bind(session: MovieSession) {
        val dateFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = dateFormatter.format(session.date)
        tvSessionTime.text = time
        if (session.available == MovieSessionAvailable.AVAILABLE) {
            ivTicketAvailability.visibility = View.GONE
        } else {
            ivTicketAvailability.visibility = View.VISIBLE
            if (session.available == MovieSessionAvailable.FEW_LEFT) {
                ivTicketAvailability.setImageResource(R.drawable.shp_few_tickets)
            } else {
                ivTicketAvailability.setImageResource(R.drawable.shp_tickets_sold)
            }
        }
    }
}