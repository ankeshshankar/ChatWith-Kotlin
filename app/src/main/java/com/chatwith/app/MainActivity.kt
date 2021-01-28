package com.chatwith.app

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.chatwith.app.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        val drawerToggle = ActionBarDrawerToggle(
                this,
                binding.sideDrawer,
                binding.toolBar,
                R.string.openDrawer,
                R.string.closeDrawer
        )
        binding.sideDrawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()


        val navView: BottomNavigationView = binding.homeBottomNavigation
        val navController = findNavController(R.id.nav_home)
        val appBarConfiguration = AppBarConfiguration(
                setOf(
                        R.id.navigation_chat,
                        R.id.navigation_activity,
                        R.id.navigation_profile
                )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.sideNavigation.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (binding.sideDrawer.isDrawerOpen(GravityCompat.START)) {
            binding.sideDrawer.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                Toast.makeText(this, "Logout", Toast.LENGTH_LONG).show()
                // Firebase.auth.signOut()
                binding.sideDrawer.closeDrawers()
                return true
            }
        }
        return true
    }
}