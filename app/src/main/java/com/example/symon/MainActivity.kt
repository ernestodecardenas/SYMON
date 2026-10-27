package com.example.symon

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.symon.databinding.ActivityMainBinding

/**
 * Activity Principal de la aplicación SYMON.
 * Configura el DrawerLayout (Menú Lateral), Toolbar con menú de 3 puntos (Overflow Menu),
 * flecha de retroceso automática en sub-pantallas y Navigation Component.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Toolbar como la Action Bar oficial de la Activity
        setSupportActionBar(binding.toolbar)

        // Aplicar insets para el Status Bar en el AppBarLayout
        ViewCompat.setOnApplyWindowInsetsListener(binding.appBarLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Destinos de nivel superior (Muestran icono de Menú Hamburguesa)
        // ScanQRFragment (R.id.nav_scan_qr) NO está en este set para mostrar la Flecha de Retroceso automáticamente
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard,
                R.id.nav_devices,
                R.id.nav_history,
                R.id.nav_settings
            ),
            binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        // Escuchar cambios de destino para gestionar visibilidad de Toolbar y Drawer
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.nav_login) {
                binding.appBarLayout.visibility = View.GONE
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                binding.appBarLayout.visibility = View.VISIBLE

                if (destination.id == R.id.nav_dashboard ||
                    destination.id == R.id.nav_devices ||
                    destination.id == R.id.nav_history ||
                    destination.id == R.id.nav_settings) {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                } else {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }
            invalidateOptionsMenu()
        }

        // Manejo de opciones del menú lateral (Drawer)
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_login -> {
                    showLogoutConfirmationDialog()
                    binding.drawerLayout.closeDrawers()
                    true
                }
                else -> {
                    val handled = androidx.navigation.ui.NavigationUI.onNavDestinationSelected(
                        menuItem,
                        navController
                    )
                    if (handled) {
                        binding.drawerLayout.closeDrawers()
                    }
                    handled
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // No mostrar el menú de 3 puntos en la pantalla de Login
        if (::navController.isInitialized && navController.currentDestination?.id == R.id.nav_login) {
            return false
        }
        menuInflater.inflate(R.menu.main_overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                showLogoutConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Está seguro de que desea salir de SYMON?")
            .setPositiveButton("Cerrar Sesión") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun performLogout() {
        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()
        navController.navigate(R.id.nav_login)
    }
}
