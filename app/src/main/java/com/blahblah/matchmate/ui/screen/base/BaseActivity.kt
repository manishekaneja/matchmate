package com.blahblah.matchmate.ui.screen.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.blahblah.matchmate.R

abstract class BaseActivity<T : ViewBinding>() : AppCompatActivity() {
    protected abstract val inflater: (LayoutInflater) -> T
    private lateinit var _binding: T

    protected val binding
        get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflater.invoke(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
        initObservers()
    }

    protected open fun initObservers() {
        // no-implementation
    }

    protected open fun initViews() {
        // no-implementation
    }
}