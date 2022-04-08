package id.go.tangerangkota.interviewalfa.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.go.tangerangkota.interviewalfa.getdata.repository.NetworkState
import id.go.tangerangkota.interviewalfa.getdata.video.MovieDetails
import id.go.tangerangkota.interviewalfa.getdata.video.VideoResult
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsViewModel (private val movieRepository : MovieDetailsRepository, movieId: Int)  : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movieDetails : LiveData<MovieDetails> by lazy {
        movieRepository.fetchMovieDetails(compositeDisposable,movieId)
    }

    val videoDetails : LiveData<VideoResult> by lazy {
        movieRepository.fetchVideo(compositeDisposable,movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }



}