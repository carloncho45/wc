package com.waytruck.cargo.ui.home

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.waytruck.cargo.R
import com.waytruck.cargo.databinding.ActivityMainBinding
import com.waytruck.cargo.slider.IndicatorView.animation.type.IndicatorAnimationType
import com.waytruck.cargo.slider.SliderAdapterExample
import com.waytruck.cargo.slider.SliderAnimations
import com.waytruck.cargo.slider.SliderItem
import com.waytruck.cargo.slider.SliderView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val phoneNumber = "+51928401266"
    val message = "Hola, necesito información"

    private var adapter: SliderAdapterExample? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        setSupportActionBar(binding.toolbar)
        loginAnonymous()
        binding.toolbar.title = ""
        getSupportActionBar()!!.setDisplayShowTitleEnabled(false);

        statusBar()
        initViewAccion()

        initSliderBanner()
        binding.includedLayout.imageSlider.setOnIndicatorClickListener {

        }

        renewItems()

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
            window.statusBarColor = ContextCompat.getColor(this, R.color.yellow)
        } else {
            window.statusBarColor = ContextCompat.getColor(this, R.color.yellow)
        }
    }

    fun initViewAccion() {
        binding.fab.setOnClickListener {
            try {
                val sendIntent = Intent(Intent.ACTION_VIEW)
                val uri = "whatsapp://send?phone=$phoneNumber&text=$message"
                sendIntent.data = Uri.parse(uri)
                startActivity(sendIntent)
            } catch (e: Exception) {
                Toast.makeText(this, "WhatsApp no está instalado", Toast.LENGTH_SHORT).show()
            }

        }
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


    fun renewItems() {
        val sliderItemList: MutableList<SliderItem> = ArrayList()
        //dummy data
        for (i in 0..2) {
            val sliderItem = SliderItem()
            //sliderItem.description = "Slider Item $i"
            when (i) {
                0 -> {
                    sliderItem.imageUrl =
                        "https://firebasestorage.googleapis.com/v0/b/lumistore-65d9b.appspot.com/o/img_4.jpg?alt=media&token=cb7d100b-4296-471e-8d45-201e4e4c8088"
                }

                1 -> {
                    sliderItem.imageUrl =
                        "https://firebasestorage.googleapis.com/v0/b/lumistore-65d9b.appspot.com/o/img_5.jpg?alt=media&token=6a9062b2-8423-43e9-968c-133ac2ab4b3e"
                }

                2 -> {
                    sliderItem.imageUrl =
                        "https://firebasestorage.googleapis.com/v0/b/lumistore-65d9b.appspot.com/o/img_6.jpg?alt=media&token=5f4010a0-ff79-4669-98da-9c8a23cad9ce"
                }
            }

            sliderItemList.add(sliderItem)
        }
        adapter!!.renewItems(sliderItemList)
    }

    fun removeLastItem(view: View?) {
        if (adapter!!.getCount() - 1 >= 0) adapter!!.deleteItem(adapter!!.getCount() - 1)
    }

    fun addNewItem(view: View?) {
        val sliderItem = SliderItem()
        sliderItem.description = "Slider Item Added Manually"
        sliderItem.imageUrl =
            "https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"
        adapter!!.addItem(sliderItem)
    }
}