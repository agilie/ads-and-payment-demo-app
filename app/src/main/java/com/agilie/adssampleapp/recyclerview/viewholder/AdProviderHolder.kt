package com.agilie.adssampleapp.recyclerview.viewholder

import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.advertising.AdProvidersType

class AdProviderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val flCellAdRoot: FrameLayout = itemView.findViewById(R.id.flCellAdRoot)
    val tvAdName: AppCompatTextView = itemView.findViewById(R.id.tvAdName)

    fun onBind(type: AdProvidersType) {
        tvAdName.text = type.name
    }
}