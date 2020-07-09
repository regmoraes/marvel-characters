package br.com.regmoraes.marvelcharacters.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.regmoraes.marvelcharacters.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = getString(R.string.app_name)

        val homeFragmentsAdapter = HomeFragmentsAdapter(this)
        viewPager.adapter = homeFragmentsAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->

            if (position == 0)
                tab.text = getString(R.string.characters_fragment_label)
            else
                tab.text = getString(R.string.favorites_fragment_label)
        }.attach()
    }
}