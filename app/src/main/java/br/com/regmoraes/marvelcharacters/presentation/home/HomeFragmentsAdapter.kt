package br.com.regmoraes.marvelcharacters.presentation.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.regmoraes.marvelcharacters.presentation.characters.CharactersFragment
import br.com.regmoraes.marvelcharacters.presentation.favorites.FavoritesFragment

class HomeFragmentsAdapter(activity: HomeActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0)
            CharactersFragment()
        else
            FavoritesFragment()
    }
}