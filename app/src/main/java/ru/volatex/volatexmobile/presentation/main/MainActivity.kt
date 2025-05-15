package ru.volatex.volatexmobile.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.volatex.volatexmobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.welcomeText.text = "Добро пожаловать!"
    }
}