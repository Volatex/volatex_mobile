package ru.volatex.volatexmobile.utils

import android.content.Context
import android.content.SharedPreferences
import com.auth0.android.jwt.JWT

class TokenManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    fun getToken(): String? = prefs.getString("token", null)

    fun isTokenValid(): Boolean {
        val token = getToken() ?: return false
        return try {
            val jwt = JWT(token)
            !jwt.isExpired(0)
        } catch (e: Exception) {
            false
        }
    }

    fun clearToken() {
        prefs.edit().clear().apply()
    }
}
