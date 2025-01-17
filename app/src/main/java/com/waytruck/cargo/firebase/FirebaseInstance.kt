package com.waytruck.cargo.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.nucleoti.lumicenter.ui.utils.PreferencesController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseInstance {
    val TAG = "FirebaseInstance"
    var db: FirebaseFirestore? = null
    var preferencesController: PreferencesController? = null
    var context: Context? = null

    // Renombra esta función
    fun updateContext(ctx: Context) {
        db = Firebase.firestore
        this.context = ctx
        preferencesController = PreferencesController(context)
    }

    // La propiedad delegada sigue igual
    var ctx: Context?
        get() = context
        set(value) {
            context = value
        }

    fun getSettings(): Flow<List<String>> = flow {
        val db = FirebaseFirestore.getInstance()

        try {
            val result = db.collection("settings")
                .document("bJfqHU0QOfBdovbwvnSZ")
                .get().await()
            val settings = result.data?.values?.flatMap { it as List<String> } ?: emptyList()
            Log.w("TAG-IMAGE-LIST", "${settings}")
            emit(settings)
        } catch (e: Exception) {
            Log.w("TAG", "Error getting documents.", e)
        }
    }

    fun getVersionApp(): Flow<Int> = flow {
        val db = FirebaseFirestore.getInstance()

        try {
            val document = db.collection("versionapp")
                .document("ZEIOp9CgHn8dmVtrLBDl").get().await()
            val version = document.getLong("version")?.toInt() ?: 0
            val updateData = document.getLong("updatedata")?.toInt() ?: 0
            if (preferencesController!!.getDataVersion() < updateData) {
                preferencesController!!.updateDataVersion(updateData)

            }

            emit(version)
        } catch (e: Exception) {
            Log.w("TAG", "Error getting documents.", e)
        }
    }


}