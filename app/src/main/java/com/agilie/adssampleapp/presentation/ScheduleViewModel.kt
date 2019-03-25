package com.agilie.adssampleapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.agilie.adssampleapp.app.AdsSampleApplication
import com.agilie.adssampleapp.data.datasource.remote.MoviesScheduleRemoteDS
import com.agilie.adssampleapp.data.repository.MoviesScheduleRepository
import com.agilie.adssampleapp.domain.model.MovieSession
import com.agilie.adssampleapp.objectbox.datasource.MoviesScheduleLocalDS
import io.reactivex.disposables.Disposable
import java.util.*

class ScheduleViewModel(
    private val cinemaId: Int,
    private val movieId: Long
) : ViewModel() {

    private var scheduleDisposable: Disposable? = null

    private val schedules = MutableLiveData<Map<Date, List<MovieSession>>>()
    private val selectedSession = MutableLiveData<MovieSession>()

    private val scheduleRepository = MoviesScheduleRepository(
        MoviesScheduleLocalDS(AdsSampleApplication.boxStore),
        MoviesScheduleRemoteDS()
    )

    init {
        scheduleDisposable = scheduleRepository.getMovieSchedule(cinemaId, movieId)
            .map { schedule ->
                schedule.groupBy {
                    Calendar.getInstance()
                        .run {
                            time = it.date
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                            time
                        }
                }
            }
            .subscribe(
                { schedules.postValue(it) },
                {

                }
            )
    }

    override fun onCleared() {
        scheduleDisposable?.dispose()
    }

    fun observeMoviesSchedule(owner: LifecycleOwner, observer: Observer<Map<Date, List<MovieSession>>>) {
        schedules.observe(owner, observer)
    }

    fun observeSessionSelection(owner: LifecycleOwner, observer: Observer<MovieSession>) {
        selectedSession.observe(owner, observer)
    }

    fun onSessionSelected(session: MovieSession) {
        selectedSession.postValue(session)
    }
}