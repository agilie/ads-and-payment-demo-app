package com.agilie.adssampleapp.advertising

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

interface AdRecyclerView {

    interface AdAdapterManager {

        /**
         * For example we used an ad item on static position - 6 (5 in list). So it will return initialCount + 1
         * In other cases, it can calculate a count of ad items entities in the source list
         *
         * @param initialCount - the size of the source items list
         * @return the final size of the items list, which includes ad items
         */
        fun getItemsCount(initialCount: Int): Int

        /**
         * Defines should an ad item be on given position
         *
         * @param position - the position of the item, which type should be defined
         * @return is the position of the ad item
         */
        fun isAdItem(position: Int): Boolean

        /**
         * Since the [RecyclerView.Adapter] does not know anything about advertising,
         * it cannot correctly define the index of an element in the source list.
         *
         * Based on knowledge about ad items this method returns the real index of an item in the source list
         *
         * @param holderPosition - the position of the [RecyclerView.ViewHolder] in the [RecyclerView.Adapter]
         * @return the index of the item in the source list
         * @throws [RuntimeException] if the given position is the position of one of the ad items
         */
        fun getItemPosition(holderPosition: Int): Int
    }

    interface AdLayoutManager {

        /**
         * Returns the [RecyclerView.LayoutManager]
         *
         * @param context - the [Context] which will be used for [RecyclerView.LayoutManager] initializing
         * @return the instance of [RecyclerView.LayoutManager]
         */
        fun getLayoutManager(context: Context): RecyclerView.LayoutManager
    }
}