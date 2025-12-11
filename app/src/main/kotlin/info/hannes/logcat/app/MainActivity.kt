package info.hannes.logcat.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import info.hannes.logcat.app.databinding.ActivityMainBinding
import info.hannes.logcat.ui.BothLogActivity
import info.hannes.logcat.ui.LogcatActivity
import info.hannes.logcat.ui.LogfileActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.containMain.toolbar)

        // enable ActionBar app icon to behave as action to toggle nav drawer
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupDrawerContent(binding.navView)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            selectNavigationItem(menuItem.itemId)
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    private fun selectNavigationItem(itemId: Int) {
        when (itemId) {
            R.id.nav_drawer_logcat -> startActivity(Intent(this, LogcatActivity::class.java))
            R.id.nav_drawer_logfile -> startActivity(Intent(this, LogfileActivity::class.java))
            R.id.nav_drawer_both_logfiles -> startActivity(Intent(this, BothLogActivity::class.java))
            R.id.nav_drawer_other_github -> {
                val url = "https://github.com/AppDevNext/Logcat"
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
            android.R.id.home -> binding.drawerLayout.openDrawer(GravityCompat.START)
            R.id.nav_drawer_logcat -> startActivity(Intent(this, LogcatActivity::class.java))
            R.id.nav_drawer_logfile -> startActivity(Intent(this, LogfileActivity::class.java))
            R.id.nav_drawer_both_logfiles -> startActivity(Intent(this, BothLogActivity::class.java))
        }
        return true
    }

}
