package com.agilie.adssampleapp.presentation

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.agilie.adssampleapp.domain.model.MovieSession
import com.agilie.adssampleapp.presentation.view.ScheduleFragment
import java.text.SimpleDateFormat
import java.util.*

class MovieSessionsPagerAdapter(
    private val cinemaId: Int,
    private val movieId: Long,
    fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {

    companion object {
        private const val TABS_DATE_FORMAT = "dd.MM"
    }

    private var sessionDates: MutableList<Date> = arrayListOf()

    override fun getItem(position: Int): Fragment {
        return ScheduleFragment.getInstance(cinemaId, movieId, sessionDates[position])
    }

    override fun getCount(): Int {
        return sessionDates.size
    }

    override fun getItemPosition(some: Any): Int {
        return POSITION_NONE
    }

    override fun saveState(): Parcelable? {
        return null
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val date = sessionDates[position]
        val formatter = SimpleDateFormat(TABS_DATE_FORMAT, Locale.getDefault())
        return formatter.format(date)
    }

    fun setSessions(sessions: Map<Date, List<MovieSession>>) {
        sessionDates = sessions.keys.toMutableList().apply {
            sortWith(
                kotlin.Comparator { date1, date2 ->
                    return@Comparator if (date1.before(date2)) {
                        -1
                    } else {
                        1
                    }
                })
        }
        notifyDataSetChanged()
    }
}