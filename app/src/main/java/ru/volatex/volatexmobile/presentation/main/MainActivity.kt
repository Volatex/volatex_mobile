package ru.volatex.volatexmobile.presentation.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.volatex.volatexmobile.data.remote.AuthApi
import ru.volatex.volatexmobile.databinding.ActivityMainBinding
import ru.volatex.volatexmobile.domain.model.AddStrategyRequest
import ru.volatex.volatexmobile.utils.TokenManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivityMainBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var api: AuthApi
    private val job = Job()
    override val coroutineContext = Dispatchers.Main + job
    private lateinit var adapter: StrategyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenManager = TokenManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(AuthApi::class.java)

        adapter = StrategyAdapter()
        binding.strategyList.layoutManager = LinearLayoutManager(this)
        binding.strategyList.adapter = adapter

        loadStrategies()

        binding.addButton.setOnClickListener {
            val figi = binding.figiInput.text.toString()
            val buyPrice = binding.buyPriceInput.text.toString().toDoubleOrNull()
            val buyQty = binding.buyQtyInput.text.toString().toIntOrNull()
            val sellPrice = binding.sellPriceInput.text.toString().toDoubleOrNull()
            val sellQty = binding.sellQtyInput.text.toString().toIntOrNull()

            if (figi.isNotBlank() && buyPrice != null && buyQty != null && sellPrice != null && sellQty != null) {
                addStrategy(figi, buyPrice, buyQty, sellPrice, sellQty)
            } else {
                Toast.makeText(this, "Заполните все поля корректно", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadStrategies() {
        launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.getStrategies("${tokenManager.getToken()}")
                }
                if (response.isSuccessful) {
                    val strategies = response.body() ?: emptyList()
                    adapter.submitList(strategies)
                } else {
                    Toast.makeText(this@MainActivity, "Ошибка загрузки", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addStrategy(figi: String, buy: Double, buyQty: Int, sell: Double, sellQty: Int) {
        val request = AddStrategyRequest(
            figi = figi,
            buy_price = buy,
            buy_quantity = buyQty,
            sell_price = sell,
            sell_quantity = sellQty
        )

        launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.addStrategy("${tokenManager.getToken()}", request)
                }
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Добавлено", Toast.LENGTH_SHORT).show()
                    loadStrategies()
                } else {
                    Toast.makeText(this@MainActivity, "Ошибка добавления", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
