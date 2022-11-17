package com.example.imageviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.fragment.findNavController
import com.example.imageviewer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navController by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
    }

    private var activeFragment = R.id.random

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bottomNavigationBar.setOnItemSelectedListener { item ->
            navigateTo(item.itemId)
            true
        }
    }

    private fun navigateTo(@IdRes navigationItemId: Int) {
        //Обратное направление
        when (activeFragment) {
            R.id.search -> {
                when (navigationItemId) {
                    R.id.random -> navController?.navigate(R.id.action_search_to_random)
                    R.id.favorite -> navController?.navigate(R.id.action_search_to_favorite)
                }
            }
            R.id.favorite ->
                when (navigationItemId) {
                    R.id.random -> navController?.navigate(R.id.action_favorite_to_random)
                    R.id.search -> navController?.navigate(R.id.action_favorite_to_search)
                }
            else -> {
                when (navigationItemId) {
                    R.id.favorite -> navController?.navigate(R.id.action_random_to_favorite)
                    R.id.search -> navController?.navigate(R.id.action_random_to_search)
                }
            }
        }
        activeFragment = navigationItemId
    }
}
