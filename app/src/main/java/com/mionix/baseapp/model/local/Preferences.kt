package com.mionix.baseapp.model.local

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.securepreferences.SecurePreferences

class Preferences(private val securePreferences: SecurePreferences) {
    companion object {
        const val APPEAL_POINT_KEY = "appeal_point_key"
        const val EMAIL ="email"
        const val PASSWORD = "password"
    }
    fun setLocalEmail(pass: String?) {
        securePreferences.edit().putString(EMAIL, pass).apply()
    }

    fun getLocalEmail() = securePreferences.getString(EMAIL, null)
    fun setLocalPassword(pass: String?) {
        securePreferences.edit().putString(PASSWORD, pass).apply()
    }

    fun getLocalPassword() = securePreferences.getString(PASSWORD, null)
//    fun setListAppealkey(appelkey: MutableList<AppealKey>){
//        val gson = Gson()
//        val json = gson.toJson(appelkey)
//        securePreferences.edit().putString(APPEAL_POINT_KEY, json).apply()
//    }
//    fun getListAppealkey():MutableList<AppealKey>? {
//        val gson = Gson()
//        val string = securePreferences.getString(APPEAL_POINT_KEY, null)
//        return gson.fromJson(string, object : TypeToken<MutableList<AppealKey>>(){}.type)
//    }
}