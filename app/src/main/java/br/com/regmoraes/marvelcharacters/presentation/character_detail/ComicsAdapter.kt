package br.com.regmoraes.marvelcharacters.presentation.character_detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.regmoraes.marvelcharacters.R
import br.com.regmoraes.marvelcharacters.model.Comic
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_comic.view.*

class ComicsAdapter : RecyclerView.Adapter<ComicsAdapter.ComicViewHolder>() {

    private var comics = listOf<Comic>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.adapter_comic, parent, false)
        return ComicViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        comics[position].apply {

            Picasso.get().load(thumbnail.pathWithExtension)
                .into(holder.itemView.comicThumbnail)

            holder.itemView.comicTitle.text = title
        }
    }

    override fun getItemCount(): Int = comics.size

    fun submitData(comics: List<Comic>) {
        this.comics = comics
        notifyDataSetChanged()
    }

    inner class ComicViewHolder(view: View) : RecyclerView.ViewHolder(view)
}