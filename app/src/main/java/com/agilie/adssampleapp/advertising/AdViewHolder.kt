package com.agilie.adssampleapp.advertising

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class AdViewHolder(view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind()

    abstract fun onRecycled()
}