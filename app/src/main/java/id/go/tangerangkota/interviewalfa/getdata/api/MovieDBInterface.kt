package id.go.tangerangkota.interviewalfa.api

import id.go.tangerangkota.interviewalfa.getdata.video.Movie
import id.go.tangerangkota.interviewalfa.getdata.video.MovieDetails
import id.go.tangerangkota.interviewalfa.getdata.video.Video
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDBInterface {

    @GET("movie/popular")
    fun getMovieList(@Query("page") page: Int): Single<Movie>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>

    @GET("movie/{movie_id}/videos")
    fun getVideo(@Path("movie_id") id: Int): Single<Video>

}