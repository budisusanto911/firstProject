package id.go.tangerangkota.interviewalfa.getdata.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import id.go.tangerangkota.interviewalfa.api.MovieDBInterface
import id.go.tangerangkota.interviewalfa.getdata.api.FIRST_PAGE
import id.go.tangerangkota.interviewalfa.getdata.video.MovieResult
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource (private val apiService : MovieDBInterface, private val compositeDisposable: CompositeDisposable)
    : PageKeyedDataSource<Int, MovieResult>(){

    private var page = FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, MovieResult>) {

        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getMovieList(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.results, null, page+1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message)
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResult>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getMovieList(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if(it.totalPages >= params.key) {
                            callback.onResult(it.results, params.key+1)
                            networkState.postValue(NetworkState.LOADED)
                        }
                        else{
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message)
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResult>) {

    }
}