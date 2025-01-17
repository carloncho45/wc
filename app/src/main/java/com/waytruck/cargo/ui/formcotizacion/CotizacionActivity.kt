package com.waytruck.cargo.ui.formcotizacion

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.waytruck.cargo.R
import com.waytruck.cargo.databinding.ActivityCotizacionBinding
import com.waytruck.cargo.ui.home.MainActivity

class CotizacionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCotizacionBinding
    val phoneNumber = "+51928401266"
    val message = "Hola, necesito información"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCotizacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBar()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Tu lógica para manejar la acción de retroceso aquí
                // Por ejemplo, cerrar la actividad:
                startMain()
            }
        })
        initViewAccion()
    }

    private fun initViewAccion() {


        //ORIGIN

        var etOriginAddress: String = ""
        var etOriginPiso: String = ""
        var bajarCarga = "";

        binding.imgOriginEscalera.setOnClickListener {
            binding.imgOriginEscalera.setBackgroundResource(R.drawable.ic_radio_button_checked)
            binding.imgOriginAscensor.setBackgroundResource(R.drawable.ic_radio_button_unchecked)
            binding.imgOriginNoRequiere.setBackgroundResource(R.drawable.ic_radio_button_unchecked)
            bajarCarga = "ESCALERA"
        }

        binding.imgOriginNoRequiere.setOnClickListener {
            binding.imgOriginNoRequiere.setBackgroundResource(R.drawable.ic_radio_button_checked)
            binding.imgOriginAscensor.setBackgroundResource(R.drawable.ic_radio_button_unchecked)
            binding.imgOriginEscalera.setBackgroundResource(R.drawable.ic_radio_button_unchecked)
            bajarCarga = "NO-REQUIERE"
        }

        binding.imgOriginAscensor.setOnClickListener {
            binding.imgOriginAscensor.setBackgroundResource(R.drawable.ic_radio_button_checked)
            binding.imgOriginNoRequiere.setBackgroundResource(R.drawable.ic_radio_button_unchecked)
            binding.imgOriginEscalera.setBackgroundResource(R.drawable.ic_radio_button_unchecked)
            bajarCarga = "ASCENSOR"
        }
        //DESTINO

        var etDestinoAddress: String = ""
        var etDestinoPiso: String = ""
        var descargarObjetos = "";

        binding.imgDestinoEscalera.setOnClickListener {
            binding.imgDestinoEscalera.setBackgroundResource(R.drawable.ic_radio_button_checked)
            binding.imgDestinoAscensor.setBackgroundResource(R.drawable.ic_radio_button_unchecked)
            binding.imgDestinoNoRequiere.setBackgroundResource(R.drawable.ic_radio_button_unchecked)
            descargarObjetos = "ESCALERA"
        }

        binding.imgDestinoAscensor.setOnClickListener {
            binding.imgDestinoAscensor.setBackgroundResource(R.drawable.ic_radio_button_checked)
            binding.imgDestinoEscalera.setBackgroundResource(R.drawable.ic_radio_button_unchecked)
            binding.imgDestinoNoRequiere.setBackgroundResource(R.drawable.ic_radio_button_unchecked)
            descargarObjetos = "ASCENSOR"
        }

        binding.imgDestinoNoRequiere.setOnClickListener {
            binding.imgDestinoNoRequiere.setBackgroundResource(R.drawable.ic_radio_button_checked)
            binding.imgDestinoEscalera.setBackgroundResource(R.drawable.ic_radio_button_unchecked)
            binding.imgDestinoAscensor.setBackgroundResource(R.drawable.ic_radio_button_unchecked)
            descargarObjetos = "NO-REQUIERE"
        }


        binding.btnCotizar.setOnClickListener {

            etOriginAddress = binding.etOriginAddress.text.toString()
            etOriginPiso = binding.etOriginPiso.text.toString()

            etDestinoAddress = binding.etDestinoAddress.text.toString()
            etDestinoPiso = binding.etDestinoPiso.text.toString()

            Log.d(
                "info-> ", etDestinoAddress + "- " + etDestinoPiso + " " + bajarCarga + " " +
                        etDestinoAddress + " " + etDestinoPiso + " " + descargarObjetos
            )
            if (!etOriginAddress.isNullOrEmpty() && !etOriginPiso.isNullOrEmpty() && !bajarCarga.isNullOrEmpty()
                && !etDestinoAddress.isNullOrEmpty() && !etDestinoPiso.isNullOrEmpty() && !descargarObjetos.isNullOrEmpty()
            ) {
                openwhatsapp(
                    etOriginAddress,
                    etOriginPiso,
                    bajarCarga,
                    etDestinoAddress,
                    etDestinoPiso,
                    descargarObjetos
                )
            }

        }
    }

    fun openwhatsapp(
        etOriginAddress: String,
        etOriginPiso: String,
        bajarCarga: String,
        etDestinoAddress: String,
        etDestinoPiso: String,
        descargarObjetos: String
    ) {
        try {
            val sendIntent = Intent(Intent.ACTION_VIEW)
            val uri = "whatsapp://send?phone=$phoneNumber&text=Hola, necesito información,\n" +
                    "*Origen:* $etOriginAddress\n *Piso:* $etOriginPiso\n *Requiere:* $bajarCarga\n" +
                    " *Destino:* $etDestinoAddress\n *Piso:* $etDestinoPiso\n *Requiere:* $descargarObjetos"
            sendIntent.data = Uri.parse(uri)
            startActivity(sendIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "WhatsApp no está instalado", Toast.LENGTH_SHORT).show()
        }
    }

    fun startMain() {
        val intent = Intent(this@CotizacionActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
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
}