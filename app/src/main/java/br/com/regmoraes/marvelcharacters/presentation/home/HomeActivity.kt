package br.com.regmoraes.marvelcharacters.presentation.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.regmoraes.marvelcharacters.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(findViewById(R.id.toolbar))

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