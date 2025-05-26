package ru.volatex.volatexmobile.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.volatex.volatexmobile.R
import ru.volatex.volatexmobile.databinding.ActivityMainBinding
import ru.volatex.volatexmobile.presentation.portfolio.PortfolioFragment
import ru.volatex.volatexmobile.presentation.tracking.TrackingFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Открыть по умолчанию TrackingFragment
        replaceFragment(TrackingFragment())

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> {
                    replaceFragment(TrackingFragment())
                    true
                }
                R.id.nav_settings -> {
                    replaceFragment(PortfolioFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }
}
