package br.com.regmoraes.marvelcharacters.presentation.character_detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.regmoraes.marvelcharacters.R
import br.com.regmoraes.marvelcharacters.model.Series
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_series.view.*

class SeriesAdapter : RecyclerView.Adapter<SeriesAdapter.ComicViewHolder>() {

    private var series = listOf<Series>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.adapter_series, parent, false)
        return ComicViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        series[position].apply {

            Picasso.get().load(thumbnail.pathWithExtension)
                .into(holder.itemView.seriesThumbnail)

            holder.itemView.seriesTitle.text = title
        }
    }

    override fun getItemCount(): Int = series.size

    fun submitData(series: List<Series>) {
        this.series = series
        notifyDataSetChanged()
    }

    inner class ComicViewHolder(view: View) : RecyclerView.ViewHolder(view)
}