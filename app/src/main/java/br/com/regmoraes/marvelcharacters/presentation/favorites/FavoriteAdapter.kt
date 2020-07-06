package br.com.regmoraes.marvelcharacters.presentation.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.regmoraes.marvelcharacters.R
import br.com.regmoraes.marvelcharacters.model.Character
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_character.view.*

class FavoriteAdapter(private val listener: OnClickListener) :
    RecyclerView.Adapter<FavoriteAdapter.CharacterViewHolder>() {

    private var favorites: List<Character> = listOf()

    fun submitData(favorites: List<Character>) {
        this.favorites = favorites.sortedBy { it.name }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.adapter_favorite, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        favorites[position].apply {

            Picasso.get().load(thumbnail.pathWithExtension)
                .into(holder.itemView.characterImageView)

            holder.itemView.characterNameTextView.text = name
        }
    }

    override fun getItemCount(): Int = favorites.size

    interface OnClickListener {
        fun onCharacterClicked(character: Character)
    }

    inner class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                favorites[bindingAdapterPosition].apply {
                    listener.onCharacterClicked(this)
                }
            }
        }
    }
}