package com.example.imageviewer.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.imageviewer.R
import com.example.imageviewer.databinding.ActivityMainBinding
import com.example.imageviewer.domain.CatImage
import com.example.imageviewer.view.utils.ContextHelper

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navController by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.opened_image)
        setContentView(binding.root)
        navController?.let {
            binding.bottomNavigationBar.setupWithNavController(it)
        }

        val image = (intent.extras?.get(ContextHelper.CAT_IMAGE_PARCEL) ?: return) as CatImage
        val bundle = Bundle()
        bundle.putParcelable(ContextHelper.CAT_IMAGE_PARCEL, image)
        navController?.navigate(R.id.favorite, bundle)
    }
}
