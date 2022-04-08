package  id.go.tangerangkota.interviewalfa.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import id.go.tangerangkota.interviewalfa.R
import id.go.tangerangkota.interviewalfa.api.MovieDBInterface
import id.go.tangerangkota.interviewalfa.getdata.api.MovieDBClient
import id.go.tangerangkota.interviewalfa.getdata.api.POSTER_BASE_URL
import id.go.tangerangkota.interviewalfa.getdata.repository.NetworkState
import id.go.tangerangkota.interviewalfa.getdata.video.MovieDetails
import id.go.tangerangkota.interviewalfa.getdata.video.VideoResult
import kotlinx.android.synthetic.main.activity_detail_movie.*
import java.text.NumberFormat
import java.util.*

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var movieRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        val movieId: Int = intent.getIntExtra("id",1)

        val apiService : MovieDBInterface = MovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, androidx.lifecycle.Observer {
            bindUI(it)
        })

        viewModel.videoDetails.observe(this, androidx.lifecycle.Observer {
            bindVideo(it)
        })

        viewModel.networkState.observe(this, androidx.lifecycle.Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE

        })
    }

    fun bindUI( it: MovieDetails){
        movie_title.text = it.title
        movie_tagline.text = it.tagline
        movie_rating.text = it.voteAverage.toString()
        movie_duration.text = it.runtime.toString() + " minutes"
        movie_overview.text = it.overview

        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(iv_movie_detail)

    }

    fun bindVideo( it: VideoResult){
        val videoKey = it.key
        layout_play.setOnClickListener {
            layout_play.visibility = View.GONE
            player_view.visibility = View.VISIBLE

            player_view.getPlayerUiController().showFullscreenButton(false)
            player_view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoKey, 0f)
                }
            })

        }

    }


    private fun getViewModel(movieId:Int): MovieDetailsViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieDetailsViewModel(movieRepository,movieId) as T
            }
        })[MovieDetailsViewModel::class.java]
    }
}
