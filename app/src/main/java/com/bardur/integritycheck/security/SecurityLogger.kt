package com.bardur.integritycheck.security

import android.util.Log

interface SecurityLogger {
    fun logMessage(tag: String, message: String)

    fun logWarning(tag: String, message: String)
}

object SecurityLoggerImpl : SecurityLogger {

    private const val LOG_TAG = "BARDUR_SECURE"

    override fun logMessage(tag: String, message: String) {
        Log.d(LOG_TAG, "$tag :: $message")
    }

    override fun logWarning(tag: String, message: String) {
        Log.w(LOG_TAG, "$tag :: $message")
    }

}