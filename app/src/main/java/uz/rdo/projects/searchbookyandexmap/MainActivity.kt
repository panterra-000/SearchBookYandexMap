package uz.rdo.projects.searchbookyandexmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import uz.rdo.projects.searchbookyandexmap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadViews()
    }

    private fun loadViews() {
        binding.apply {
        val navController = findNavController(R.id.navHost)
        bottomMenuNav.setupWithNavController(navController)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

}