package ru.volatex.volatexmobile.presentation.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
