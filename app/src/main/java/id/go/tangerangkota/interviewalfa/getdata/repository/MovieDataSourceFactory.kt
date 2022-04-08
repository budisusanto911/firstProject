package id.go.tangerangkota.interviewalfa.getdata.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import id.go.tangerangkota.interviewalfa.api.MovieDBInterface
import id.go.tangerangkota.interviewalfa.getdata.video.MovieResult

import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory (private val apiService : MovieDBInterface, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, MovieResult>() {

    val moviesLiveDataSource =  MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, MovieResult> {
        val movieDataSource = MovieDataSource(apiService,compositeDisposable)

        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}