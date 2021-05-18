package com.example.airquality

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation : BottomNavigationView = findViewById(R.id.bottom_navigation) //might have to be initalized before
        bottomNavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_search -> {
                    supportActionBar!!.title = "Søk"
                    openFragment(SearchFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_fav -> {
                    supportActionBar!!.title = "Favoritter"
                    openFragment(FavFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_info -> {
                    supportActionBar!!.title = "Info"
                    openFragment(InfoFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
        supportActionBar!!.title = "Search"
        openFragment(SearchFragment.newInstance())
    }
    // Fragment function
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}