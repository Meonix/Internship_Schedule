package com.mionix.baseapp.model.local

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.securepreferences.SecurePreferences

class Preferences(private val securePreferences: SecurePreferences) {
    companion object {
        const val APPEAL_POINT_KEY = "appeal_point_key"
    }
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