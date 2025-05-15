package ru.volatex.volatexmobile.presentation.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.volatex.volatexmobile.data.remote.AuthApi
import ru.volatex.volatexmobile.databinding.ActivityLoginBinding
import ru.volatex.volatexmobile.domain.model.SignInRequest
import ru.volatex.volatexmobile.presentation.LauncherActivity
import ru.volatex.volatexmobile.utils.TokenManager
import kotlin.coroutines.CoroutineContext

class LoginActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var api: AuthApi
    private lateinit var tokenManager: TokenManager

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenManager = TokenManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(AuthApi::class.java)

        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signIn(email, password)
            } else {
                Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signIn(email: String, password: String) {
        launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.signIn(SignInRequest(email, password))
                }

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        tokenManager.saveToken(body.token)
                        Toast.makeText(this@LoginActivity, "Успешный вход!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@LoginActivity, LauncherActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Ошибка: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
