package com.example.airquality

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    var search: SearchFragment? = null
    var fav: FavFragment? = null
    var info: InfoFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_AirQuality)
        setContentView(R.layout.activity_main)

        val bottomNavigation : BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_search -> {
                    if(search == null){
                        search = SearchFragment.newInstance()
                    }
                    openFragment(search!!, true)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_fav -> {
                    if(fav == null){
                        fav = FavFragment.newInstance()
                    }
                    openFragment(fav!!, true)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_info -> {
                    if(info == null){
                        info = InfoFragment.newInstance()
                    }
                    openFragment(info!!, true)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
        bottomNavigation.setOnNavigationItemReselectedListener(BottomNavigationView.OnNavigationItemReselectedListener { item ->
            when (item.itemId) {
                R.id.navigation_search -> {
                    return@OnNavigationItemReselectedListener
                }
                R.id.navigation_fav -> {
                    return@OnNavigationItemReselectedListener
                }
                R.id.navigation_info -> {
                    return@OnNavigationItemReselectedListener
                }
            }
        })
        openFragment(SearchFragment.newInstance(), false)
    }
    // Fragment function
    private fun openFragment(fragment: Fragment, back: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        if(back){
            if(supportFragmentManager.backStackEntryCount > 0){
                if(!supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name.equals(fragment.toString())){
                    transaction.addToBackStack(fragment.toString())
                }
            } else if(supportFragmentManager.backStackEntryCount == 0 && fragment == search){
                // passes through if the if statement above holds
            } else{
                transaction.addToBackStack(fragment.toString())
            }
        }
        transaction.commit()
    }
}