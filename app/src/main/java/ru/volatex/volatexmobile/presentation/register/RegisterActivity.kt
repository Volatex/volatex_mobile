package ru.volatex.volatexmobile.presentation.register

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import android.util.Log
import android.view.View
import android.widget.TextView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.volatex.volatexmobile.R
import ru.volatex.volatexmobile.data.remote.AuthApi
import ru.volatex.volatexmobile.data.repository.AuthRepositoryImpl
import ru.volatex.volatexmobile.databinding.ActivityRegisterBinding
import ru.volatex.volatexmobile.domain.usecase.RegisterUseCase.RegisterUseCase
import ru.volatex.volatexmobile.presentation.verify.VerifyEmailActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    private val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(AuthApi::class.java)
        val repo = AuthRepositoryImpl(api)
        val useCase = RegisterUseCase(repo)
        viewModel = RegisterViewModel(useCase)

        // Внутри onCreate после binding.apply { ... }
        val spannable = SpannableString("Уже есть аккаунт? Войти")
        val loginClickable = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@RegisterActivity, ru.volatex.volatexmobile.presentation.login.LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

// Делаем слово "Войти" кликабельным (индекс по тексту)
        val loginStart = spannable.indexOf("Войти")
        val loginEnd = loginStart + "Войти".length
        spannable.setSpan(loginClickable, loginStart, loginEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.root.findViewById<TextView>(R.id.loginTextView).apply {
            text = spannable
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT // убираем цвет подсветки при клике
        }

        binding.apply {
            emailInput.doAfterTextChanged { viewModel.email.value = it.toString() }
            passwordInput.doAfterTextChanged { viewModel.password.value = it.toString() }
            confirmPasswordInput.doAfterTextChanged { viewModel.confirmPassword.value = it.toString() }

            viewModel.isFormValid.observe(this@RegisterActivity) {
                registerButton.isEnabled = it
            }

            registerButton.setOnClickListener {
                viewModel.register()
            }

            viewModel.registrationResult.observe(this@RegisterActivity) { result ->
                result.onSuccess { response ->
                    Log.d(TAG, "Registration successful: ${response.toString()}")

                    val email = viewModel.email.value ?: ""
                    val intent = Intent(this@RegisterActivity, VerifyEmailActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish()
                }.onFailure { exception ->
                    Log.e(TAG, "Registration failed: ${exception.message}")
                    Toast.makeText(this@RegisterActivity, exception.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
