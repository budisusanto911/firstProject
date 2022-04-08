package id.go.tangerangkota.interviewalfa.getdata.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.go.tangerangkota.interviewalfa.api.MovieDBInterface
import id.go.tangerangkota.interviewalfa.getdata.repository.NetworkState
import id.go.tangerangkota.interviewalfa.getdata.video.MovieDetails
import id.go.tangerangkota.interviewalfa.getdata.video.VideoResult

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailsNetworkDataSource (private val apiService : MovieDBInterface, private val compositeDisposable: CompositeDisposable) {

    private val _networkState  = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadedMovieDetailsResponse =  MutableLiveData<MovieDetails>()
    val downloadedMovieResponse: LiveData<MovieDetails>
        get() = _downloadedMovieDetailsResponse

    private val _downloadedVideoResponse =  MutableLiveData<VideoResult>()
    val downloadedVideoResponse: LiveData<VideoResult>
        get() = _downloadedVideoResponse

    fun fetchMovieDetails(movieId: Int) {

        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailsDataSource", it.message)
                        }
                    )
            )

        }

        catch (e: Exception){
            Log.e("MovieDetailsDataSource",e.message)
        }
    }

    fun fetchVideo(movieId: Int) {

        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getVideo(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedVideoResponse.postValue(it.results[0])
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("VideoDataSource", it.message)
                        }
                    )
            )

        }

        catch (e: Exception){
            Log.e("VideoDataSource",e.message)
        }
    }
}