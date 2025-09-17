package com.bardur.integritycheck.security


interface EnvironmentValidator {
    fun isValid(): Boolean
}

class EnvironmentValidatorImpl(
    private val rootChecker: RootChecker,
    private val emulatorChecker: EmulatorChecker,
    private val appIntegrityChecker: AppIntegrityChecker,
    private val securityLogger: SecurityLogger = SecurityLoggerImpl,
    private val debug: Boolean = false,
) : EnvironmentValidator {

    companion object {
        private const val TAG = "EnvironmentValidator"
    }

    override fun isValid(): Boolean {
        if (debug) {
            securityLogger.logMessage(TAG, "Running on debug variant, skipping check!")
            return true
        }

        if (rootChecker.isRooted()) {
            securityLogger.logWarning(TAG, "Device is rooted!")
            return false
        } else {
            securityLogger.logMessage(TAG, "Device is not rooted!")
        }


        if (emulatorChecker.isEmulator()) {
            securityLogger.logWarning(TAG, "Device is emulator!")
            return false
        } else {
            securityLogger.logMessage(TAG, "Device is not emulator!")
        }

        if (!appIntegrityChecker.isValid()) {
            securityLogger.logWarning(TAG, "App Integrity check failed!")
            return false
        } else {
            securityLogger.logMessage(TAG, "App Integrity check passed!")
        }

        return true
    }

}