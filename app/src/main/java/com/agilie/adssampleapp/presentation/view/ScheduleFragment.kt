package com.agilie.adssampleapp.presentation.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.agilie.adssampleapp.R
import com.agilie.adssampleapp.domain.model.MovieSession
import com.agilie.adssampleapp.presentation.MovieSessionsAdapter
import com.agilie.adssampleapp.presentation.ScheduleViewModel
import com.agilie.adssampleapp.presentation.ScheduleViewModelFactory
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class ScheduleFragment : Fragment() {

    companion object {
        const val ARGS_KEY_CINEMA_ID = "com.agilie.adssampleapp.presentation.view.ScheduleFragment.ARGS_KEY_CINEMA_ID"
        const val ARGS_KEY_MOVIE_ID = "com.agilie.adssampleapp.presentation.view.ScheduleFragment.ARGS_KEY_MOVIE_ID"
        const val ARGS_KEY_DATE = "com.agilie.adssampleapp.presentation.view.ScheduleFragment.ARGS_KEY_DATE"

        fun getInstance(cinemaId: Int, movieId: Long, date: Date): ScheduleFragment =
            ScheduleFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARGS_KEY_CINEMA_ID, cinemaId)
                    putLong(ARGS_KEY_MOVIE_ID, movieId)
                    putSerializable(ARGS_KEY_DATE, date)
                }
            }
    }

    private var isActionBlocked = AtomicBoolean(false)
    private val userActionHandler = Handler(Looper.getMainLooper())
    private val userActionDelay = 150L

    private var viewModel: ScheduleViewModel? = null
    private var date: Date? = null

    private val sessionClickListener = { pos: Int, session: MovieSession ->
        if (!isActionBlocked.get()) {
            isActionBlocked.set(true)
            val action: () -> Unit = { viewModel?.onSessionSelected(session) }
            userActionHandler.postDelayed(getRunnable(action), userActionDelay)
        }
    }
    private var scheduleAdapter = MovieSessionsAdapter(sessionClickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        date = arguments?.getSerializable(ARGS_KEY_DATE) as? Date

        val cinemaId = arguments?.getInt(ARGS_KEY_CINEMA_ID)
        val movieId = arguments?.getLong(ARGS_KEY_MOVIE_ID)

        if (cinemaId != null && movieId != null) {
            viewModel = ViewModelProviders
                .of(activity ?: return, ScheduleViewModelFactory(cinemaId, movieId))
                .get(ScheduleViewModel::class.java)
                .apply {
                    observeMoviesSchedule(this@ScheduleFragment, Observer { onScheduleReceived(it) })
                }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.rvSessionsList)?.run {
            setHasFixedSize(true)
            adapter = scheduleAdapter
        }

    }

    private fun onScheduleReceived(schedule: Map<Date, List<MovieSession>>) {
        val date = this.date
        if (date != null) {
            scheduleAdapter.sessions = schedule[date] ?: return
        }
    }

    private fun getRunnable(action: () -> Unit): Runnable {
        return Runnable {
            action()
            isActionBlocked.set(false)
        }
    }
}