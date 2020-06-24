package info.hannes.logcat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import info.hannes.logcat.sample.R

class MainActivity : AppCompatActivity() {

    private lateinit var mDrawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDrawerLayout = findViewById(R.id.drawer_layout)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // enable ActionBar app icon to behave as action to toggle nav drawer
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        if (navigationView != null) {
            setupDrawerContent(navigationView)
        }

    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            selectNavigationItem(menuItem.itemId)
            mDrawerLayout.closeDrawers()
            true
        }

    }

    private fun selectNavigationItem(itemId: Int) {

        when (itemId) {
            R.id.nav_drawer_logcat -> startActivity(Intent(this, LogcatActivity::class.java))
            R.id.nav_drawer_logfile -> startActivity(Intent(this, LogfileActivity::class.java))
            R.id.nav_drawer_both_logfiles -> startActivity(Intent(this, BothLogActivity::class.java))
            R.id.nav_drawer_other_github -> {
                val url = "https://github.com/hannesa2/Logcat"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.drawer_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
            }
            R.id.nav_drawer_logcat -> {
                startActivity(Intent(this, LogcatActivity::class.java))
            }
            R.id.nav_drawer_logfile -> {
                startActivity(Intent(this, LogfileActivity::class.java))
            }
            R.id.nav_drawer_both_logfiles -> {
                startActivity(Intent(this, BothLogActivity::class.java))
            }
        }

        return true
    }

}
