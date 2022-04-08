package id.go.tangerangkota.interviewalfa.ui.details

import androidx.lifecycle.LiveData
import id.go.tangerangkota.interviewalfa.api.MovieDBInterface
import id.go.tangerangkota.interviewalfa.getdata.repository.MovieDetailsNetworkDataSource
import id.go.tangerangkota.interviewalfa.getdata.repository.NetworkState
import id.go.tangerangkota.interviewalfa.getdata.video.MovieDetails
import id.go.tangerangkota.interviewalfa.getdata.video.VideoResult
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository (private val apiService : MovieDBInterface) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchMovieDetails (compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {

        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService,compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse

    }

    fun fetchVideo (compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<VideoResult> {

        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService,compositeDisposable)
        movieDetailsNetworkDataSource.fetchVideo(movieId)

        return movieDetailsNetworkDataSource.downloadedVideoResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }



}