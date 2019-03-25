package com.agilie.adssampleapp.adform

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agilie.adssampleapp.advertising.AdRecyclerView

class AdFormAdLayoutManager(private val adAdapterManager: AdFormAdAdapterManager) : AdRecyclerView.AdLayoutManager {
    private val columnCount = 3

    override fun getLayoutManager(context: Context): RecyclerView.LayoutManager {
        return GridLayoutManager(context, columnCount, RecyclerView.VERTICAL, false)
            .apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (adAdapterManager.isAdItem(position)) {
                            columnCount
                        } else {
                            1
                        }
                    }
                }
            }
    }
}