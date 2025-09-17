package com.bardur.integritycheck.security

import android.content.Context
import android.os.Build

interface AppIntegrityChecker {
    fun isValid(): Boolean
}

class AppIntegrityCheckerImpl(
    private val context: Context,
    private val securityLogger: SecurityLogger = SecurityLoggerImpl
) : AppIntegrityChecker {

    companion object {
        private const val TAG = "AppIntegrity"
        private const val PACKAGE_NAME = "com.bardur.integritycheck"
    }

    override fun isValid(): Boolean {
        // Check if someone has changed package name
        if (!isPackageValid()) {
            securityLogger.logWarning(TAG, "Package name is invalid")
            return false
        }

        if (!isInstallerValid()) {
            // Log a warning if the user is using an app without installer
            securityLogger.logWarning(TAG, "No Installer found!")
            return false
        }

        return true
    }

    private fun isPackageValid(): Boolean {
        return context.packageName.compareTo(PACKAGE_NAME) == 0
    }

    private fun isInstallerValid(): Boolean {
        val installer: String? = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                context.packageManager.getInstallSourceInfo(PACKAGE_NAME).installingPackageName
            } else {
                context.packageManager.getInstallerPackageName(PACKAGE_NAME)
            }
        } catch (e: Exception) {
            null
        }

        securityLogger.logMessage(TAG, "Installer : $installer")
        return installer != null
    }
}