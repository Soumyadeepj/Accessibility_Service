package com.example.myaccessibilityservice
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent

class MyAccessibilityService : AccessibilityService() {
    private val TAG = "MyAccessibilityService"


    override fun onInterrupt() {
        Log.d(TAG,"onInterrupt: something went wrong")
    }



    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.e(TAG, "onAccessibilityEvent: ")

        val packageName = event?.packageName.toString()
        val packageManager = this.packageManager
        try {
            val applicationInfo: ApplicationInfo = packageManager.getApplicationInfo(packageName, 0)
            val applicationLabel: CharSequence = packageManager.getApplicationLabel(applicationInfo)
            Log.e(TAG, "app name is : $applicationLabel")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }


    override fun onServiceConnected() {
        val info = AccessibilityServiceInfo()
        info.apply {

            eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED or AccessibilityEvent.TYPE_VIEW_FOCUSED

            feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN

            notificationTimeout = 100
        }

        this.serviceInfo = info
        Log.e(TAG,"OnServiceConnected:")

    }
}
