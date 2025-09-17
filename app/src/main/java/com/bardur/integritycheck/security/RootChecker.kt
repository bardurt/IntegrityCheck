package com.bardur.integritycheck.security

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.io.File

interface RootChecker {
    fun isRooted(): Boolean
}

class RootCheckerImpl(
    private val context: Context,
    private val securityLogger: SecurityLogger = SecurityLoggerImpl
) : RootChecker {

    companion object {
        const val TAG = "RootChecker"
        private const val ROOT_ACCESS = "su"
        private val ROOT_APPS = listOf(
            "com.noshufou.android.su",
            "com.noshufou.android.su.elite",
            "eu.chainfire.supersu",
            "com.koushikdutta.superuser",
            "com.thirdparty.superuser",
            "com.yellowes.su",
            "com.topjohnwu.magisk",
            "com.kingroot.kinguser",
            "com.kingo.root",
            "com.smedialink.oneclickroot",
            "com.zhiqupk.root.global",
            "com.alephzain.framaroot"
        )
        private val SU_DIRECTORIES = listOf(
            "/sbin/",
            "/system/bin/",
            "/system/xbin/",
            "/data/local/xbin/",
            "/data/local/bin/",
            "/system/sd/xbin/",
            "/system/bin/failsafe/",
            "/data/local/"
        )

        private const val TEST_KEYS_TAG = "test-keys"
    }

    override fun isRooted(): Boolean {
        val isRoot = findSuBinary()
        val hasRootAps = checkRootFilesAndPackages(context)
        val testTags = checkTags()

        securityLogger.logMessage(TAG, "Rooted : $isRoot")
        securityLogger.logMessage(TAG, "Has Rooted Apps : $hasRootAps")
        securityLogger.logMessage(TAG, "Test Tags : $testTags")

        if (isRoot) {
            return true
        }

        if (hasRootAps) {
            return true
        }

        if (testTags) {
            return true
        }

        return false
    }

    private fun findSuBinary(): Boolean {
        var found = false

        for (dir in SU_DIRECTORIES) {
            val file = File(dir + ROOT_ACCESS)
            if (file.exists()) {
                securityLogger.logWarning(TAG, "Found suspicious file ${file.absolutePath}")
                found = true
                break
            }
        }

        return found
    }

    private fun checkRootFilesAndPackages(context: Context): Boolean {
        var result = false
        for (s in ROOT_APPS) {
            if (isPackageInstalled(s, context)) {
                result = true
                break
            }
        }

        return result
    }

    private fun isPackageInstalled(
        name: String,
        context: Context
    ): Boolean {
        val pm = context.packageManager
        return try {
            pm.getPackageInfo(
                name,
                PackageManager.GET_ACTIVITIES
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun checkTags(): Boolean {
        val tag = Build.TAGS
        return tag != null && tag.trim { it <= ' ' }.contains(TEST_KEYS_TAG)
    }

}