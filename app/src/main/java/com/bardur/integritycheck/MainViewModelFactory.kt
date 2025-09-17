package com.bardur.integritycheck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bardur.integritycheck.security.EnvironmentValidator

class MainViewModelFactory(
    private val environmentValidator: EnvironmentValidator,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(environmentValidator = environmentValidator) as T
        } else {
            throw RuntimeException("Cannot create ViewModel for ${modelClass::class.java}")
        }
    }
}