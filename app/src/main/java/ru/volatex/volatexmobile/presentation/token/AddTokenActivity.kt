package ru.volatex.volatexmobile.presentation.token

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.volatex.volatexmobile.databinding.ActivityAddTokenBinding
import ru.volatex.volatexmobile.data.remote.AuthApi
import ru.volatex.volatexmobile.domain.model.AddTokenRequest
import ru.volatex.volatexmobile.presentation.main.MainActivity
import ru.volatex.volatexmobile.utils.TokenManager

class AddTokenActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivityAddTokenBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var api: AuthApi
    private val job = Job()
    override val coroutineContext = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTokenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenManager = TokenManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(AuthApi::class.java)

        binding.saveTokenButton.setOnClickListener {
            val tinkoffToken = binding.tokenInput.text.toString()
            if (tinkoffToken.isNotBlank()) {
                sendTokenToServer(tinkoffToken)
            } else {
                Toast.makeText(this, "Введите токен", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun sendTokenToServer(tinkoffToken: String) {
        launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.addTinkoffToken(
                        authHeader = "${tokenManager.getToken()}",
                        tokenRequest = AddTokenRequest(tinkoffToken)
                    )
                }

                if (response.isSuccessful) {
                    Toast.makeText(this@AddTokenActivity, "Токен успешно сохранен", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@AddTokenActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this@AddTokenActivity, "Ошибка при сохранении", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AddTokenActivity, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
