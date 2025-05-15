package ru.volatex.volatexmobile.presentation.verify

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
import ru.volatex.volatexmobile.databinding.ActivityVerifyEmailBinding
import ru.volatex.volatexmobile.domain.model.VerifyEmailRequest
import com.google.gson.GsonBuilder
import ru.volatex.volatexmobile.presentation.login.LoginActivity
import kotlin.coroutines.CoroutineContext

class VerifyEmailActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivityVerifyEmailBinding
    private lateinit var email: String
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var api: AuthApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = intent.getStringExtra("email") ?: ""

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(AuthApi::class.java)

        binding.verifyButton.setOnClickListener {
            val code = binding.codeInput.text.toString().trim()
            if (code.isNotEmpty()) {
                verifyEmail(code)
            } else {
                Toast.makeText(this, "Введите код", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyEmail(code: String) {
        val request = VerifyEmailRequest(code = code, email = email)
        launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.verifyEmail(request)
                }

                if (response.isSuccessful) {
                    Toast.makeText(this@VerifyEmailActivity, "Email подтвержден!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@VerifyEmailActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@VerifyEmailActivity, "Ошибка: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@VerifyEmailActivity, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
