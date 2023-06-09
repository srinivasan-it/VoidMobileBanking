package com.example.voidmobilebanking.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.voidmobilebanking.R


class MainActivity : AppCompatActivity() {

    private val navHostFragment by lazy {
        supportFragmentManager.fragments.first() as NavHostFragment
    }

    val navController by lazy {
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}