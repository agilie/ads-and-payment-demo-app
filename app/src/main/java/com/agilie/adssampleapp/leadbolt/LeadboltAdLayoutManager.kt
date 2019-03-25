package com.agilie.adssampleapp.leadbolt

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agilie.adssampleapp.advertising.AdRecyclerView

class LeadboltAdLayoutManager : AdRecyclerView.AdLayoutManager {
    private val columnCount = 3

    override fun getLayoutManager(context: Context): RecyclerView.LayoutManager {
        return GridLayoutManager(context, columnCount, RecyclerView.VERTICAL, false)
    }
}