package id.go.tangerangkota.interviewalfa.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import id.go.tangerangkota.interviewalfa.R
import id.go.tangerangkota.interviewalfa.api.MovieDBInterface
import id.go.tangerangkota.interviewalfa.getdata.api.MovieDBClient
import id.go.tangerangkota.interviewalfa.getdata.repository.NetworkState
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    lateinit var movieRepository: MoviePagedListRepository
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val searchItem: MenuItem = menu!!.findItem(R.id.action_search)
        val searchView: SearchView = MenuItemCompat.getActionView(searchItem) as SearchView

        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(searchQuery: String): Boolean {

                return true
            }
        })

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService : MovieDBInterface = MovieDBClient.getClient()

        movieRepository = MoviePagedListRepository(apiService)

        viewModel = getViewModel()

        val movieAdapter = MoviePagedListAdapter(this)

        val gridLayoutManager = GridLayoutManager(this, 2)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if (viewType == movieAdapter.MOVIE_VIEW_TYPE) return  1
                else return 2
            }
        };


        rv.layoutManager = gridLayoutManager
        rv.setHasFixedSize(true)
        rv.adapter = movieAdapter

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })

    }


    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(movieRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }

}
