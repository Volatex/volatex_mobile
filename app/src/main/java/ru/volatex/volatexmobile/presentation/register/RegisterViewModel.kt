package ru.volatex.volatexmobile.presentation.register

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.volatex.volatexmobile.domain.model.RegisterRequest
import ru.volatex.volatexmobile.domain.usecase.RegisterUseCase.RegisterUseCase

class RegisterViewModel(private val registerUseCase: RegisterUseCase) : ViewModel() {

    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val confirmPassword = MutableLiveData("")

    val isFormValid = MediatorLiveData<Boolean>().apply {
        addSource(email) { validateForm() }
        addSource(password) { validateForm() }
        addSource(confirmPassword) { validateForm() }
    }

    private fun validateForm() {
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS
        val emailValid = emailPattern.matcher(email.value ?: "").matches()
        val pass = password.value
        val confirm = confirmPassword.value
        isFormValid.value = emailValid && !pass.isNullOrBlank() && pass == confirm
    }

    val registrationResult = MutableLiveData<Result<Unit>>()

    fun register() {
        val emailValue = email.value ?: return
        val passwordValue = password.value ?: return
        viewModelScope.launch {
            val result = registerUseCase(RegisterRequest(emailValue, passwordValue))
            registrationResult.value = result.map { Unit }
        }
    }
}