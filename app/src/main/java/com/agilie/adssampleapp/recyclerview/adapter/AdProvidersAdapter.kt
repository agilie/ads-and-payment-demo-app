package com.agilie.adssampleapp.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.advertising.AdProvidersType
import com.agilie.adssampleapp.recyclerview.viewholder.AdProviderHolder

class AdProvidersAdapter(
    private val adProviders: List<AdProvidersType>,
    private val clickListener: ((id: Int, type: AdProvidersType) -> Unit)? = null
) : RecyclerView.Adapter<AdProviderHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): AdProviderHolder {
        return AdProviderHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_ad_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return adProviders.size
    }

    override fun onBindViewHolder(holder: AdProviderHolder, position: Int) {
        holder.onBind(adProviders[position])
        holder.flCellAdRoot.setOnClickListener {
            val pos = holder.adapterPosition
            clickListener?.invoke(pos, adProviders[pos])
        }
    }

    override fun onViewRecycled(holder: AdProviderHolder) {
        holder.flCellAdRoot.setOnClickListener(null)
    }

}