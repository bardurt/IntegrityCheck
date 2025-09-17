package com.bardur.integritycheck

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bardur.integritycheck.security.EnvironmentValidator

class MainViewModel(private val environmentValidator: EnvironmentValidator) : ViewModel() {

    private val _viewState = MutableLiveData(ViewState())

    val viewState: LiveData<ViewState>
        get() = _viewState

    init {
        checkDevice()
    }

    private fun checkDevice() {
        if (environmentValidator.isValid()) {
            _viewState.value =
                _viewState.value?.copy(navigationState = NavigationState.ValidEnvironment)
        } else {
            _viewState.value =
                _viewState.value?.copy(navigationState = NavigationState.InvalidEnvironment)
        }
    }

    data class ViewState(
        val navigationState: NavigationState = NavigationState.None
    )

    sealed class NavigationState {
        object None : NavigationState()
        object InvalidEnvironment : NavigationState()
        object ValidEnvironment : NavigationState()
    }
}