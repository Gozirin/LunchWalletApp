package com.example.lunchwallet

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.lunchwallet.common.authentication.UserDatastore
import com.example.lunchwallet.common.logout.presentation.LogoutViewModel
import com.example.lunchwallet.databinding.ActivityMainBinding
import com.example.lunchwallet.util.ADMIN
import com.example.lunchwallet.util.BENEFICIARY
import com.example.lunchwallet.util.KITCHENSTAFF
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.validation.checkUserType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userDatastore: UserDatastore
    private val logoutViewModel: LogoutViewModel by viewModels()
    private var email: String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userDatastore = UserDatastore(this)

        userDatastore.userEmail.asLiveData().observe(this) {
            if (it != null) {
                email = it
            } else {
                Toast.makeText(this, "Code is empty", Toast.LENGTH_SHORT).show()
            }
        }

//        userDatastore.authToken.asLiveData().observe(this) { token ->
//            if (token != null) {
//                when (email?.let { checkUserType(it) }) {
//                    BENEFICIARY -> findNavController(R.id.fragmentContainerView).navigate(R.id.foodBeneficiaryDashboardFragment)
//                    ADMIN -> findNavController(R.id.fragmentContainerView).navigate(R.id.dashboardFragment)
//                    KITCHENSTAFF -> findNavController(R.id.fragmentContainerView).navigate(R.id.kitchenStaffDashBoardFragment)
//                }
////                findNavController(R.id.fragmentContainerView).currentBackStackEntryFlow
////                findNavController(R.id.fragmentContainerView).currentBackStackEntry
//            }else {
//                findNavController(R.id.fragmentContainerView).navigate(R.id.loginFragment)
//            }
//        }
        setToolBar()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.navigation_drawer_menu, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val navController = findNavController(R.id.fragmentContainerView)
//        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
//    }

    private fun closeDrawer() {
        binding.apply {
            mainActivityDrawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun openDrawer() {
        binding.apply {
            mainActivityDrawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun inflateLogoutView() {
        closeDrawer()
        val view = View.inflate(this@MainActivity, R.layout.logout_confirmation, null)
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setView(view)

        val dialog = builder.create()

        dialog.apply {
            show()
            window?.attributes?.apply {
                gravity = Gravity.TOP
            }
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            Log.d(TAG, "inflateLogoutView: email is $email")

            val dismissBtn = findViewById<Button>(R.id.dismiss_logout_btn)
            val logoutBtn = findViewById<Button>(R.id.logout_btn)

            logoutBtn?.apply {
                setOnClickListener {
                    logOutUser(email!!)
                    dismiss()
//                    Toast.makeText(this@MainActivity, "Logged out!", Toast.LENGTH_SHORT).show()
                }
            }

            dismissBtn?.apply {
                setOnClickListener {
                    dismiss()
//                    Toast.makeText(this@MainActivity, "Dismissed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setToolBar() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{_, destination, _ ->
            if (destination.id == R.id.foodBeneficiaryDashboardFragment ||
                destination.id == R.id.notificationsScreenFragment || destination.id == R.id.qrCodeScanner
            ) {
                binding.mainActivityToolbar.visibility = View.VISIBLE
            }else{
                binding.  mainActivityToolbar.visibility = View.GONE
            }

        }
        binding.apply {

//            toggle = ActionBarDrawerToggle(this@MainActivity, mainActivityDrawerLayout, R.string.openDrawerContentDesc, R.string.closeDrawerContentDesc)
//            mainActivityDrawerLayout.addDrawerListener(toggle)
//
//            toggle.syncState()
//
//            supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.foodBeneficiaryDashboardFragment -> {
                        closeDrawer()
                        navController.navigate(R.id.foodBeneficiaryDashboardFragment)
//                        Toast.makeText(this@MainActivity, "menu_item_meal_time_table", Toast.LENGTH_SHORT).show()
                    }
                    R.id.notificationsScreenFragment-> {
                        closeDrawer()
                        navController.navigate(R.id.notificationsScreenFragment)
//                        Toast.makeText(this@MainActivity, "menu_item_notification", Toast.LENGTH_SHORT).show()
                    }
                    R.id.menu_item_logout -> {
                        inflateLogoutView()
//                        Toast.makeText(this@MainActivity, "menu_item_logout", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }

            toolbarIcHambuger.setOnClickListener {
                openDrawer()
            }

            navView.getHeaderView(0).findViewById<ImageView>(R.id.menu_component_close_vector)?.setOnClickListener {
                closeDrawer()
            }
        }
    }


    fun setStatusBarColor(color: Int) {
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }

    // extension function to hide soft keyboard programmatically
    fun Activity.hideSoftKeyboard() {
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

//    private fun getUserEmail(): String? {
//    lifecycleScope.launch(Dispatchers.IO) {
//        userDatastore.getUserEmail().catch { e ->
//            e.printStackTrace()
//        }.collect {
//                email = it
//                Log.d(TAG, "onViewCreated: datastore,id: $email")
//
//            }
//        }
//        return email
//    }

    private fun logOutUser(email: String){
        lifecycleScope.launch {
        when (checkUserType(email)) {
            BENEFICIARY -> logoutViewModel.logoutBeneficiary(email)
            ADMIN -> logoutViewModel.logoutAdmin(email)
            KITCHENSTAFF -> logoutViewModel.logoutKitchenStaff(email)
        }
            userDatastore.clearFromDataStore()
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                logoutViewModel.logoutState.collect { logoutUIState ->
                    when (logoutUIState) {
                        is Resource.Success -> {
                            findNavController(R.id.fragmentContainerView).navigate(R.id.loginFragment)
                        }
                        is Resource.Error -> {
                            Toast.makeText(
                                this@MainActivity,
                                logoutUIState.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        is Resource.Loading -> {

                        }
                    }
                }
            }
        }
    }

  private  fun inflateLogoutViewX() {
        val view = View.inflate(this@MainActivity, R.layout.logout_confirmation, null)
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setView(view)

        val dialog = builder.create()

        dialog.apply {
            show()
            window?.attributes?.apply {
                gravity = Gravity.TOP
            }
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            Log.d(TAG, "inflateLogoutView: email is $email")

            val dismissBtn = findViewById<Button>(R.id.dismiss_logout_btn)
            val logoutBtn = findViewById<Button>(R.id.logout_btn)

            logoutBtn?.apply {
                setOnClickListener {
                    logOutUser(email!!)
                    dismiss()
//                    Toast.makeText(this@MainActivity, "Logged out!", Toast.LENGTH_SHORT).show()
                }
            }

            dismissBtn?.apply {
                setOnClickListener {
                    dismiss()
//                    Toast.makeText(this@MainActivity, "Dismissed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout -> inflateLogoutViewX()
        }
        return super.onOptionsItemSelected(item)
    }
}
