package com.agilie.adssampleapp.adform

import com.agilie.adssampleapp.advertising.AdRecyclerView

class AdFormAdAdapterManager : AdRecyclerView.AdAdapterManager {

    private val adCount = 2
    private val adFirstPosition = 6
    private val adLastPosition = 16

    override fun getItemsCount(initialCount: Int): Int {
        return initialCount + adCount
    }

    override fun isAdItem(position: Int): Boolean {
        return position == adFirstPosition || position == adLastPosition
    }

    override fun getItemPosition(holderPosition: Int): Int {
        return when {
            holderPosition < adFirstPosition -> holderPosition
            holderPosition < adLastPosition -> holderPosition - 1
            holderPosition > adLastPosition -> holderPosition - adCount
            else -> throw RuntimeException("The given position shouldn't be the position of an ad!")
        }
    }
}