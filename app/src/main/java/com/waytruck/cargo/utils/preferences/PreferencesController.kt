package com.nucleoti.lumicenter.ui.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

private const val SESION = "SESION"
private const val KEY_EMAIL_SOCIAL_LOGIN = "email_social_login"
private const val IS_LOGIN = "isLogin"
private const val UPDATE_DATA_VERSION = "UPDATE_DATA_VERSION"

class PreferencesController(context: Context?) {

    private val prefs: SharedPreferences


    fun getPrefs(): SharedPreferences {
        return prefs
    }


    init {
        prefs = context!!.getSharedPreferences("way-cargo-preferences", Context.MODE_PRIVATE)
    }


    fun saveEmailSocialLogin(value: String?) {
        prefs.edit().putString(KEY_EMAIL_SOCIAL_LOGIN, value).apply()
    }

    fun getEmailSocialLogin(): String? {
        return prefs.getString(KEY_EMAIL_SOCIAL_LOGIN, null)
    }

    fun setLogin(value: Boolean) {
        prefs.edit().putBoolean(IS_LOGIN, value).apply()
    }


    fun isLogin(): Boolean {
        return prefs.getBoolean(IS_LOGIN, false);
    }

    fun deletePreferences() {
        prefs.edit().clear().apply()
    }

    fun updateDataVersion(updateData: Int) {
        prefs.edit().putInt(UPDATE_DATA_VERSION, updateData).apply()
    }

    fun getDataVersion(): Int {
        return prefs.getInt(UPDATE_DATA_VERSION, 0)
    }

}
