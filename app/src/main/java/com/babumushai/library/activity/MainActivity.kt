package com.babumushai.library.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.babumushai.library.R
import com.babumushai.library.fragment.AboutAppFragment
import com.babumushai.library.fragment.DashboardFragment
import com.babumushai.library.fragment.FavouritesFragment
import com.babumushai.library.fragment.ProfileFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var navigationView: NavigationView
    lateinit var frameLayout: FrameLayout

    var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigationView)
        frameLayout = findViewById(R.id.frame)

        // setting action bar as tool bar
        setUpToolbar()

        // setting DASHBOARD on home screen
        openDashboard()

        // functioning of HAMBURGER icon or action bar drawer toggle icon
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity, drawerLayout, R.string.open_drawer, R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)   // adding toggle to icon
        actionBarDrawerToggle.syncState()   // synchronising icon with drawer(icon will show back button when drawer is active)

        // setting listener to menu icon in navigation view
        navigationView.setNavigationItemSelectedListener {

            // to mark menu items checked
            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false  // unchecking previous menu item
            }
            it.isCheckable = true   // checking if current menu item is checkable or not
            it.isChecked = true // if checkable then check
            previousMenuItem = it   /*
                                 now setting it as previous menu item,
                                 to be unchecked in future
                                     */

            when (it.itemId) {    // using IT: will give us the currently selected item

                R.id.dashboard -> {
                    Toast.makeText(this@MainActivity, "Dashboard", Toast.LENGTH_SHORT)
                        .show()

                    openDashboard()
                    drawerLayout.closeDrawers()
                }

                R.id.profile -> {
                    Toast.makeText(this@MainActivity, "Profile", Toast.LENGTH_SHORT).show()
                    // starting the fragment transaction,
                    supportFragmentManager.beginTransaction().replace(R.id.frame, ProfileFragment())
                        .commit()

                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers() // closing the drawers once transaction for fragment committed
                }

                R.id.favourites -> {
                    Toast.makeText(this@MainActivity, "Favourites", Toast.LENGTH_SHORT)
                        .show()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FavouritesFragment()).commit()

                    supportActionBar?.title = "Favourites"
                    drawerLayout.closeDrawers()
                }

                R.id.aboutApp -> {
                    Toast.makeText(this@MainActivity, "About App", Toast.LENGTH_SHORT)
                        .show()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, AboutAppFragment()).commit()

                    supportActionBar?.title = "About App"
                    drawerLayout.closeDrawers()
                }

                R.id.LogOut -> {
                    Toast.makeText(this@MainActivity, "Logged Out", Toast.LENGTH_SHORT)
                        .show()

                    //  jumping to login activity
                    val intent = Intent(this@MainActivity, LogInActivity::class.java)
                    startActivity(intent)

                    // setting share preferences to false
                    val sharedPref =
                        getSharedPreferences(
                            getString(R.string.preference_name),
                            Context.MODE_PRIVATE
                        )
                    sharedPref.edit().putBoolean("isLoggedIn", false).apply()

                    drawerLayout.closeDrawers()
                }

            }

            return@setNavigationItemSelectedListener true   // return boolean value to tell the item is selected
        }
    }

    // making toolbar perform as actionbar

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)    // enable home button and set active
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   // to display home button
    }

    private fun openDashboard() {
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.frame, DashboardFragment())
        transaction.commit()
        supportActionBar?.title = "Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)   // previously checking item
    }

    // click listener to HAMBURGER icon
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId  // string id of the item in variable

        if (id == android.R.id.home) {  // checking id is equal to the id of HAMBURGER icon (placed on home button)
            drawerLayout.openDrawer(GravityCompat.START)    // drawer slide from left side
        }

        return super.onOptionsItemSelected(item)
    }

    // customizing behaviour of back press button
    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        // if fragment is not dashboard, back to dashboard
        // else default behaviour
        when (frag) {
            !is DashboardFragment -> openDashboard()  // back to dashboard
            else -> super.onBackPressed()
        }
    }

}