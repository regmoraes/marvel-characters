package br.com.regmoraes.marvelcharacters.presentation.characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import br.com.regmoraes.marvelcharacters.R
import br.com.regmoraes.marvelcharacters.model.Character
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.character_adapter.view.*

class CharacterAdapter(private val listener: OnClickListener) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>(), Filterable {

    private val charactersMap = mutableMapOf<Long, Character>()
    private val character = mutableListOf<Character>()
    private val charactersFilter by lazy { CharacterFilter() }
    private var filteredCharacters: List<Character> = charactersMap.values.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.character_adapter, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        filteredCharacters[position].apply {
            Picasso.get().load(thumbnail.pathWithExtension)
                .into(holder.itemView.characterImageView)

            holder.itemView.characterNameTextView.text = name

            if (isFavorite)
                holder.itemView.favoriteImageView.setImageResource((R.drawable.ic_baseline_star_24))
            else
                holder.itemView.favoriteImageView.setImageResource((R.drawable.ic_baseline_star_border_24))
        }
    }

    override fun getItemCount(): Int = filteredCharacters.size

    fun submitData(characters: List<Character>, refresh: Boolean = false) {
        if (refresh) this.charactersMap.clear()
        characters.forEach { charactersMap[it.id] = it }
        filteredCharacters = charactersMap.values.toList()
        notifyDataSetChanged()
    }

    fun updateFavoriteStatus(character: Character) {
        if (charactersMap.containsKey(character.id))
            charactersMap[character.id] = character
        filteredCharacters = charactersMap.values.toList()
        notifyDataSetChanged()
    }

    inner class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                listener.onCharacterClicked(filteredCharacters[bindingAdapterPosition])
            }
            view.favoriteImageView.setOnClickListener {
                listener.onFavoriteClicked(filteredCharacters[bindingAdapterPosition])
            }
        }
    }

    override fun getFilter(): Filter = charactersFilter

    private inner class CharacterFilter : Filter() {

        override fun performFiltering(constraint: CharSequence): FilterResults {

            val filteredCharacters = if (constraint.isBlank()) {
                charactersMap.values
            } else {
                charactersMap.values.filter {
                    it.name.contains(
                        constraint.toString(),
                        ignoreCase = true
                    )
                }
            }
            return FilterResults().apply {
                values = filteredCharacters
                count = filteredCharacters.size
            }
        }

        override fun publishResults(
            constraint: CharSequence,
            results: FilterResults
        ) {
            filteredCharacters =
                results.values as List<Character>
            notifyDataSetChanged()
        }
    }


    interface OnClickListener {
        fun onFavoriteClicked(character: Character)
        fun onCharacterClicked(character: Character)
    }
}