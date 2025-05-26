package ru.volatex.volatexmobile.presentation.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.volatex.volatexmobile.data.remote.AuthApi
import ru.volatex.volatexmobile.databinding.FragmentPortfolioBinding
import ru.volatex.volatexmobile.domain.model.Position
import ru.volatex.volatexmobile.utils.TokenManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PortfolioFragment : Fragment() {

    private var _binding: FragmentPortfolioBinding? = null
    private val binding get() = _binding!!

    private lateinit var api: AuthApi
    private lateinit var tokenManager: TokenManager
    private lateinit var adapter: PortfolioAdapter
    private val positions = mutableListOf<Position>() // Хранение данных

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPortfolioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация API и TokenManager
        tokenManager = TokenManager(requireContext())
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(AuthApi::class.java)

        // Настраиваем RecyclerView
        adapter = PortfolioAdapter(positions)
        binding.portfolioList.layoutManager = LinearLayoutManager(requireContext())
        binding.portfolioList.adapter = adapter

        // Загружаем данные
        loadPortfolio()
    }

    private fun loadPortfolio() {
        lifecycleScope.launch {
            try {
                val response = api.getPortfolio("${tokenManager.getToken()}")
                if (response.isSuccessful) {
                    val newPositions = response.body() ?: emptyList()
                    positions.clear()
                    positions.addAll(newPositions)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Ошибка загрузки портфеля", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
