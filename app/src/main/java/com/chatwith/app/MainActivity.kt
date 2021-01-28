package com.chatwith.app

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.chatwith.app.databinding.ActivityMainBinding
import com.chatwith.app.databinding.SideMenuHeaderBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingSideMenu: SideMenuHeaderBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingSideMenu = SideMenuHeaderBinding.inflate(layoutInflater)
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

        auth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = auth.currentUser


        //Setting up data from auth of current user
        val navigationView: NavigationView = findViewById(R.id.side_navigation)
        val topView = navigationView.getHeaderView(0)
        val userName = topView.findViewById<TextView>(R.id.userName)
        val userEmail = topView.findViewById<TextView>(R.id.useEmail)
        val userImage = topView.findViewById<ImageView>(R.id.userImage)
        userName.text = user?.displayName.toString()
        userEmail.text = user?.email.toString()
        Glide.with(this)
                .load(user?.photoUrl)
                .into(userImage)


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
                Toast.makeText(this, "Logout Successfully", Toast.LENGTH_LONG).show()
                auth.signOut()
                binding.sideDrawer.closeDrawers()
                finish()
                return true
            }
        }
        return true
    }
}