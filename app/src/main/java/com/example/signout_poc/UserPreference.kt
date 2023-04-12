package com.example.signout_poc

import android.content.Context
import android.content.SharedPreferences
import com.example.signout_poc.Constants.FORMATTED_DATE
import com.example.signout_poc.Constants.KEY_NAME
import com.example.signout_poc.Constants.KEY_PASSWORD
import com.example.signout_poc.Constants.SHARED_PREFERENCES

class UserPreference(context: Context) {

    private val mySharedPreference = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor? = mySharedPreference.edit()

    fun getUserName(): String? {
        return mySharedPreference.getString(KEY_NAME,null)
    }

    fun getUserPassword(): String? {
        return mySharedPreference.getString(KEY_PASSWORD, null)
    }

    fun setUserName(name: String?) {
        editor?.putString(KEY_NAME, name)?.apply()
    }

    fun setUserPassword(password: String?) {
        editor?.putString(KEY_PASSWORD, password)?.apply()
    }

    fun getRequestTime(): String? {
        return mySharedPreference.getString(FORMATTED_DATE, null)
    }

    fun setRequestTime(requestTime: String?) {
        editor?.putString(FORMATTED_DATE, requestTime)?.apply()
    }

    fun clearData() {
        editor?.clear()
        editor?.apply()
    }
}