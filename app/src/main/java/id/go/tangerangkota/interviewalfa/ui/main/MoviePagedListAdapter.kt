package id.go.tangerangkota.interviewalfa.ui.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.go.tangerangkota.interviewalfa.R
import id.go.tangerangkota.interviewalfa.getdata.api.POSTER_BASE_URL
import id.go.tangerangkota.interviewalfa.getdata.repository.NetworkState
import id.go.tangerangkota.interviewalfa.getdata.video.MovieResult
import id.go.tangerangkota.interviewalfa.ui.details.MovieDetailsActivity
import kotlinx.android.synthetic.main.item_movie.view.*
import kotlinx.android.synthetic.main.item_network_state.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class MoviePagedListAdapter(public val context: Context) : PagedListAdapter<MovieResult, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.item_movie, parent, false)
            return MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.item_network_state, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position),context)
        }
        else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }


    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }




    class MovieDiffCallback : DiffUtil.ItemCallback<MovieResult>() {
        override fun areItemsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
            return oldItem == newItem
        }

    }


    class MovieItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: MovieResult?,context: Context) {
            itemView.movie_title.text = movie?.title
            var tanggalkeluar = movie?.releaseDate
            val tanggal: List<String>? = tanggalkeluar?.split("-")
            val format = SimpleDateFormat("yyyy-MM-dd")
            val dateFormat = SimpleDateFormat("MMM")
            try {
                val date: Date = format.parse(movie?.releaseDate)
                itemView.idbulan.text  =dateFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            itemView.idtanggal.text = tanggal?.get(2)
            itemView.idtahun.text = tanggal?.get(0)
            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(itemView.iv_movie)

            itemView.setOnClickListener{
                val intent = Intent(context, MovieDetailsActivity::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }

        }

    }

    class NetworkStateItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progress_bar_item.visibility = View.VISIBLE;
            }
            else  {
                itemView.progress_bar_item.visibility = View.GONE;
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = networkState.msg;
            }
            else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = networkState.msg;
            }
            else {
                itemView.error_msg_item.visibility = View.GONE;
            }
        }
    }


    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }

    }

}