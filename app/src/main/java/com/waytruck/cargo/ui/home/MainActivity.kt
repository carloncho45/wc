package com.waytruck.cargo.ui.home

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nucleoti.lumicenter.ui.utils.PreferencesController
import com.waytruck.cargo.BuildConfig
import com.waytruck.cargo.R
import com.waytruck.cargo.databinding.ActivityMainBinding
import com.waytruck.cargo.firebase.FirebaseInstance
import com.waytruck.cargo.slider.IndicatorView.animation.type.IndicatorAnimationType
import com.waytruck.cargo.slider.SliderAdapterExample
import com.waytruck.cargo.slider.SliderAnimations
import com.waytruck.cargo.slider.SliderView
import com.waytruck.cargo.ui.formcotizacion.CotizacionActivity
import com.waytruck.cargo.ui.login.LoginActivity
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private var adapter: SliderAdapterExample? = null
    private lateinit var auth: FirebaseAuth
    val firebaseInstance = FirebaseInstance()
    var preferencesController: PreferencesController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseInstance.updateContext(this)

        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        setSupportActionBar(binding.toolbar)

        getSupportActionBar()!!.setDisplayShowTitleEnabled(false);
        preferencesController = PreferencesController(this)

        loginAnonymous()
        binding.toolbar.title = ""
        collectVersionApp()
        collectSettings()
        //  firebaseInstance.getSettings()
        statusBar()
        initViewAccion()

        initSliderBanner()
        binding.includedLayout.imageSlider.setOnIndicatorClickListener {

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private fun collectSettings() {
        // Usa el lifecycleScope para colectar el Flow
        lifecycleScope.launch {
            firebaseInstance.getSettings().collect { settingsList ->
                // Aquí puedes manejar los datos obtenidos
                renewItems(settingsList)
            }
        }
    }

    private fun collectVersionApp() {
        // Usa el lifecycleScope para colectar el Flow
        lifecycleScope.launch {
            firebaseInstance.getVersionApp().collect { version ->
                // Aquí puedes manejar los datos obtenidos

                if (version > BuildConfig.VERSION_CODE) {
                    val dialogFragment = PurchaseConfirmationDialogFragment()
                    dialogFragment.show(
                        supportFragmentManager,
                        PurchaseConfirmationDialogFragment.TAG
                    )
                }

            }
        }
    }

    fun goToUpdate() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("market://details?id=com.waytruck.cargo")
        //  intent.data = Uri.parse(preferencesController.getLinkUpdateApp())
        startActivity(intent)

    }

    fun statusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
            window.statusBarColor = ContextCompat.getColor(this, R.color.dark_primary_color)
        } else {
            window.statusBarColor = ContextCompat.getColor(this, R.color.dark_primary_color)
        }
    }

    fun initViewAccion() {
        binding.fab.setOnClickListener {

            startCotizacion()
        }
        binding.imgLogout.setOnClickListener {
            logout()
        }
    }

    fun startCotizacion(){
        val intent = Intent(this@MainActivity, CotizacionActivity::class.java)
        startActivity(intent)
        finish()
    }


    fun loginAnonymous() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Log.d("TAG", "signInAnonymously:success ")
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInAnonymously:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    //updateUI(null)
                }
            }
    }

    fun initSliderBanner() {
        adapter = SliderAdapterExample(this)
        binding.includedLayout.imageSlider.setSliderAdapter(adapter!!)
        binding.includedLayout.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!

        binding.includedLayout.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.includedLayout.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH)
        binding.includedLayout.imageSlider.setIndicatorSelectedColor(Color.WHITE)
        binding.includedLayout.imageSlider.setIndicatorUnselectedColor(Color.GRAY)
        binding.includedLayout.imageSlider.setScrollTimeInSec(3)
        binding.includedLayout.imageSlider.setAutoCycle(true)
        binding.includedLayout.imageSlider.startAutoCycle()
    }


    fun renewItems(images: List<String>) {
        val sliderItemList: MutableList<String> = ArrayList()
        //dummy data
        adapter!!.renewItems(images)
    }

    fun logout() {
        auth.signOut()
        preferencesController!!.deletePreferences()
        starLogin()
    }

    fun starLogin() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun removeLastItem(view: View?) {
        if (adapter!!.getCount() - 1 >= 0) adapter!!.deleteItem(adapter!!.getCount() - 1)
    }

    fun addNewItem(view: View?) {
        val sliderItem =
            "https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"
        adapter!!.addItem(sliderItem)
    }
}