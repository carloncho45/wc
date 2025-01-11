package com.waytruck.cargo.ui.login

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.nucleoti.lumicenter.ui.utils.PreferencesController
import com.waytruck.cargo.BuildConfig
import com.waytruck.cargo.databinding.ActivityLoginBinding
import com.waytruck.cargo.ui.home.MainActivity


//@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 9001

    //  private var callbackManager: CallbackManager? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mAuth: FirebaseAuth? = null
    private var credentialGoogle: AuthCredential? = null
    private var preferencesController: PreferencesController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        preferencesController = PreferencesController(this)

        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.TOKEN_GOOGLE_SIGNIN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(applicationContext, gso)
        mGoogleSignInClient!!.signOut()
        preferencesController = PreferencesController(this)

        initViewAccion()
    }

    fun sigInGoogle() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        resultLauncher.launch(signInIntent)

    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    preferencesController!!.saveEmailSocialLogin(account!!.email)
                    firebaseAuthWithGoogle(account)
                } catch (exception: ApiException) {
                    exception.message?.let { Log.d("WelcomeFragment", it) }
                }
            }
        }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        // val activity: Activity? = activity
        credentialGoogle = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credentialGoogle!!)
            .addOnCompleteListener(this) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    preferencesController!!.setLogin(true)
                    startHome()
                } else {
                    if (task.exception != null) {
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            // showVinculate(SocialType.GOOGLE)
                        } else {
                            task.exception!!.message?.let {
                                Log.d(
                                    "WelcomeFragment",
                                    it
                                )
                            }
                        }
                    }
                }
            }

    }

    private fun initViewAccion() {
        binding.lnLoginGoogle.setOnClickListener {
            sigInGoogle()
        }
        binding.txtLoginGoogle.setOnClickListener {
            sigInGoogle()
        }
        binding.lnLoginAnonimo.setOnClickListener {
            loginAnonymous()
        }
        binding.txtLoginAnonimo.setOnClickListener {
            loginAnonymous()
        }
    }

    fun loginAnonymous() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    startHome()
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

    fun startHome() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}