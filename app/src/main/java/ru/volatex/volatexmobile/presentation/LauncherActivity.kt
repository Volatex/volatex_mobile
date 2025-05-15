package ru.volatex.volatexmobile.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.volatex.volatexmobile.presentation.login.LoginActivity
import ru.volatex.volatexmobile.presentation.main.MainActivity
import ru.volatex.volatexmobile.presentation.register.RegisterActivity
import ru.volatex.volatexmobile.utils.TokenManager

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenManager = TokenManager(this)
        val intent = if (tokenManager.isTokenValid()) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, RegisterActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}
