package ru.volatex.volatexmobile.presentation.verify

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.volatex.volatexmobile.data.remote.AuthApi
import ru.volatex.volatexmobile.databinding.ActivityVerifyEmailBinding
import ru.volatex.volatexmobile.domain.model.VerifyEmailRequest
import ru.volatex.volatexmobile.presentation.login.LoginActivity
import com.google.gson.GsonBuilder
import kotlin.coroutines.CoroutineContext

class VerifyEmailActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivityVerifyEmailBinding
    private lateinit var email: String
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var api: AuthApi
    private lateinit var codeInputs: List<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = intent.getStringExtra("email") ?: ""

        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        api = retrofit.create(AuthApi::class.java)

        codeInputs = listOf(
            binding.codeContainer.getChildAt(0) as EditText,
            binding.codeContainer.getChildAt(1) as EditText,
            binding.codeContainer.getChildAt(2) as EditText,
            binding.codeContainer.getChildAt(3) as EditText,
            binding.codeContainer.getChildAt(4) as EditText,
            binding.codeContainer.getChildAt(5) as EditText,
        )

        setupCodeInputs()

        binding.verifyButton.setOnClickListener {
            val code = codeInputs.joinToString("") { it.text.toString() }
            if (code.length == 6) {
                verifyEmail(code)
            } else {
                Toast.makeText(this, "Введите 6-значный код", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signInLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun setupCodeInputs() {
        for (i in codeInputs.indices) {
            codeInputs[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && i < codeInputs.size - 1) {
                        codeInputs[i + 1].requestFocus()
                    } else if (s?.isEmpty() == true && i > 0) {
                        codeInputs[i - 1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
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
                    startActivity(Intent(this@VerifyEmailActivity, LoginActivity::class.java))
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
