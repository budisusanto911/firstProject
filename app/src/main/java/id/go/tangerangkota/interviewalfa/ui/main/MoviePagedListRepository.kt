package id.go.tangerangkota.interviewalfa.ui.main

import id.go.tangerangkota.interviewalfa.api.MovieDBInterface
import id.go.tangerangkota.interviewalfa.getdata.api.POST_PER_PAGE
import id.go.tangerangkota.interviewalfa.getdata.repository.MovieDataSource
import id.go.tangerangkota.interviewalfa.getdata.repository.MovieDataSourceFactory
import id.go.tangerangkota.interviewalfa.getdata.repository.NetworkState
import id.go.tangerangkota.interviewalfa.getdata.video.MovieResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository (private val apiService : MovieDBInterface) {

    lateinit var moviePagedList: LiveData<PagedList<MovieResult>>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<MovieResult>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState)
    }
}