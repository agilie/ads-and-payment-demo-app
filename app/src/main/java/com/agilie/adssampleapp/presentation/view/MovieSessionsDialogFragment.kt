package com.agilie.adssampleapp.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.presentation.MovieSessionsPagerAdapter
import com.agilie.adssampleapp.presentation.ScheduleViewModel
import com.agilie.adssampleapp.presentation.ScheduleViewModelFactory
import com.google.android.material.tabs.TabLayout

class MovieSessionsDialogFragment : DialogFragment() {

    companion object {
        const val ARGS_KEY_CINEMA_ID =
            "com.agilie.adssampleapp.presentation.view.MovieSessionsDialogFragment.ARGS_KEY_CINEMA_ID"
        const val ARGS_KEY_MOVIE_ID =
            "com.agilie.adssampleapp.presentation.view.MovieSessionsDialogFragment.ARGS_KEY_MOVIE_ID"

        fun getInstance(cinemaId: Int, movieId: Long): MovieSessionsDialogFragment =
            MovieSessionsDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARGS_KEY_CINEMA_ID, cinemaId)
                    putLong(ARGS_KEY_MOVIE_ID, movieId)
                }
            }
    }


    private var viewModel: ScheduleViewModel? = null
    private lateinit var sessionsAdapter: MovieSessionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cinemaId = arguments?.getInt(ARGS_KEY_CINEMA_ID)
        val movieId = arguments?.getLong(ARGS_KEY_MOVIE_ID)
        if (cinemaId != null && movieId != null) {
            sessionsAdapter = MovieSessionsPagerAdapter(cinemaId, movieId, childFragmentManager)
            viewModel = ViewModelProviders
                .of(activity ?: return, ScheduleViewModelFactory(cinemaId, movieId))
                .get(ScheduleViewModel::class.java)
                .apply {
                    observeMoviesSchedule(this@MovieSessionsDialogFragment, Observer {
                        sessionsAdapter.setSessions(it)
                    })
                }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_movie_sessions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager: ViewPager = view.findViewById<ViewPager>(R.id.vpSchedulersList).apply {
            adapter = sessionsAdapter
        }
        view.findViewById<TabLayout>(R.id.tlTabs)?.run { setupWithViewPager(viewPager) }

    }
}