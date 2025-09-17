package com.bardur.integritycheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.bardur.integritycheck.security.AppIntegrityCheckerImpl
import com.bardur.integritycheck.security.EmulatorCheckerImpl
import com.bardur.integritycheck.security.EnvironmentValidatorImpl
import com.bardur.integritycheck.security.RootCheckerImpl
import com.bardur.integritycheck.security.SecurityLogger
import com.bardur.integritycheck.security.SecurityLoggerImpl
import com.bardur.integritycheck.ui.theme.AppIntegrityCheckTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel;
    private lateinit var viewModelFactory: MainViewModelFactory;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val securityLogger: SecurityLogger = SecurityLoggerImpl
        viewModelFactory = MainViewModelFactory(
            EnvironmentValidatorImpl(
                rootChecker =
                    RootCheckerImpl(
                        context = this,
                        securityLogger = securityLogger
                    ),
                emulatorChecker = EmulatorCheckerImpl(securityLogger = securityLogger),
                appIntegrityChecker = AppIntegrityCheckerImpl(
                    context = this,
                    securityLogger = securityLogger
                ),
                securityLogger = securityLogger,
                debug = false
            ),
        )

        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            AppIntegrityCheckTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StartUp(viewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun StartUp(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.viewState.observeAsState()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state?.navigationState) {
            MainViewModel.NavigationState.InvalidEnvironment -> {
                Text("Environment Invalid")
            }

            MainViewModel.NavigationState.None -> {
                Text("Environment Valid")
            }

            MainViewModel.NavigationState.ValidEnvironment -> {
                CircularProgressIndicator()
            }

            null -> {
                CircularProgressIndicator()
            }
        }
    }
}

